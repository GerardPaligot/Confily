package com.paligot.confily.backend.quiz

import com.paligot.confily.backend.ConflictException
import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.internals.infrastructure.exposed.DatabaseFactory
import com.paligot.confily.backend.quiz.application.QuizRepositoryExposed
import com.paligot.confily.backend.quiz.infrastructure.exposed.QuizPlayersTable
import com.paligot.confily.models.inputs.QuizAnswerInput
import com.paligot.confily.models.inputs.QuizPlayerInput
import com.paligot.confily.models.inputs.QuizSubmissionInput
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class QuizRepositoryExposedTest {
    private lateinit var database: Database
    private lateinit var repository: QuizRepositoryExposed

    @BeforeTest
    fun setup() {
        database = DatabaseFactory.createTestDatabase()
        repository = QuizRepositoryExposed(database)
    }

    @Test
    fun `registerPlayer creates a player and returns its id`() = runBlocking {
        val eventId = QuizTestFixtures.createEvent(database)

        val playerId = repository.registerPlayer(
            eventId.toString(),
            QuizPlayerInput(username = "Alice", deviceId = "device-1")
        )

        assertTrue(playerId.isNotBlank())
        val count = transaction(database) { QuizPlayersTable.selectAll().count() }
        assertEquals(1, count)
    }

    @Test
    fun `registerPlayer with same device updates username without duplicating`() = runBlocking {
        val eventId = QuizTestFixtures.createEvent(database)

        val first = repository.registerPlayer(
            eventId.toString(),
            QuizPlayerInput(username = "Alice", deviceId = "device-1")
        )
        val second = repository.registerPlayer(
            eventId.toString(),
            QuizPlayerInput(username = "Alice Renamed", deviceId = "device-1")
        )

        assertEquals(first, second)
        val count = transaction(database) { QuizPlayersTable.selectAll().count() }
        assertEquals(1, count)
    }

    @Test
    fun `questionsByCode returns ordered questions with options and no correctness leak`() = runBlocking {
        val eventId = QuizTestFixtures.createEvent(database)
        val partnerId = QuizTestFixtures.createPartner(database, eventId, quizCode = "ABCD")
        QuizTestFixtures.createQuestion(
            database, eventId, partnerId,
            question = "Capital of France?",
            options = listOf("Paris", "Lyon", "Nice"),
            correctIndex = 0,
            order = 0
        )

        val questions = repository.questionsByCode(eventId.toString(), "ABCD")

        assertEquals(1, questions.size)
        assertEquals("Capital of France?", questions[0].question)
        assertEquals(listOf("Paris", "Lyon", "Nice"), questions[0].options.map { it.label })
    }

    @Test
    fun `questionsByCode throws NotFound for an unknown code`() {
        val eventId = QuizTestFixtures.createEvent(database)
        assertFailsWith<NotFoundException> {
            runBlocking { repository.questionsByCode(eventId.toString(), "ZZZZ") }
        }
    }

    @Test
    fun `submit grades answers server-side and returns counts`() = runBlocking {
        val eventId = QuizTestFixtures.createEvent(database)
        val partnerId = QuizTestFixtures.createPartner(database, eventId, quizCode = "ABCD")
        val q1 = QuizTestFixtures.createQuestion(
            database, eventId, partnerId, "Q1", listOf("right", "wrong"), correctIndex = 0, order = 0
        )
        val q2 = QuizTestFixtures.createQuestion(
            database, eventId, partnerId, "Q2", listOf("wrong", "right"), correctIndex = 1, order = 1
        )
        repository.registerPlayer(eventId.toString(), QuizPlayerInput("Alice", "device-1"))

        val q1Correct = QuizTestFixtures.answerIds(database, q1)[0]
        val q2Wrong = QuizTestFixtures.answerIds(database, q2)[0]
        val result = repository.submit(
            eventId.toString(),
            "ABCD",
            QuizSubmissionInput(
                deviceId = "device-1",
                answers = listOf(
                    QuizAnswerInput(q1.toString(), q1Correct.toString()),
                    QuizAnswerInput(q2.toString(), q2Wrong.toString())
                )
            )
        )

        assertEquals(2, result.totalCount)
        assertEquals(1, result.correctCount)
        assertEquals(2, result.perQuestion.size)
        assertEquals(true, result.perQuestion.first { it.questionId == q1.toString() }.isCorrect)
        assertEquals(false, result.perQuestion.first { it.questionId == q2.toString() }.isCorrect)
    }

    @Test
    fun `submit a second time for the same partner is rejected with Conflict`() {
        val eventId = QuizTestFixtures.createEvent(database)
        val partnerId = QuizTestFixtures.createPartner(database, eventId, quizCode = "ABCD")
        val q1 = QuizTestFixtures.createQuestion(
            database, eventId, partnerId, "Q1", listOf("right", "wrong"), correctIndex = 0
        )
        runBlocking { repository.registerPlayer(eventId.toString(), QuizPlayerInput("Alice", "device-1")) }
        val answer = QuizTestFixtures.answerIds(database, q1)[0]
        val input = QuizSubmissionInput("device-1", listOf(QuizAnswerInput(q1.toString(), answer.toString())))
        runBlocking { repository.submit(eventId.toString(), "ABCD", input) }

        assertFailsWith<ConflictException> {
            runBlocking { repository.submit(eventId.toString(), "ABCD", input) }
        }
    }

    @Test
    fun `submit with an unregistered device is rejected with NotFound`() {
        val eventId = QuizTestFixtures.createEvent(database)
        val partnerId = QuizTestFixtures.createPartner(database, eventId, quizCode = "ABCD")
        val q1 = QuizTestFixtures.createQuestion(
            database, eventId, partnerId, "Q1", listOf("right", "wrong"), correctIndex = 0
        )
        val answer = QuizTestFixtures.answerIds(database, q1)[0]

        assertFailsWith<NotFoundException> {
            runBlocking {
                repository.submit(
                    eventId.toString(),
                    "ABCD",
                    QuizSubmissionInput("unknown-device", listOf(QuizAnswerInput(q1.toString(), answer.toString())))
                )
            }
        }
    }

    @Test
    fun `leaderboard ranks by score desc then by earliest completion`() = runBlocking {
        val eventId = QuizTestFixtures.createEvent(database)
        val partnerId = QuizTestFixtures.createPartner(database, eventId, quizCode = "ABCD")
        val q1 = QuizTestFixtures.createQuestion(
            database, eventId, partnerId, "Q1", listOf("right", "wrong"), correctIndex = 0
        )
        val correct = QuizTestFixtures.answerIds(database, q1)[0]
        val wrong = QuizTestFixtures.answerIds(database, q1)[1]

        repository.registerPlayer(eventId.toString(), QuizPlayerInput("Alice", "device-a"))
        repository.registerPlayer(eventId.toString(), QuizPlayerInput("Bob", "device-b"))
        repository.submit(
            eventId.toString(), "ABCD",
            QuizSubmissionInput("device-a", listOf(QuizAnswerInput(q1.toString(), correct.toString())))
        )
        repository.submit(
            eventId.toString(), "ABCD",
            QuizSubmissionInput("device-b", listOf(QuizAnswerInput(q1.toString(), wrong.toString())))
        )

        val board = repository.leaderboard(eventId.toString())

        assertEquals(2, board.size)
        assertEquals("Alice", board[0].username)
        assertEquals(1, board[0].score)
        assertEquals(1, board[0].rank)
        assertEquals("Bob", board[1].username)
        assertEquals(0, board[1].score)
        assertEquals(2, board[1].rank)
    }
}
