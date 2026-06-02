package com.paligot.confily.backend.quiz

import com.paligot.confily.backend.internals.infrastructure.exposed.DatabaseFactory
import com.paligot.confily.backend.quiz.infrastructure.exposed.QuizQuestionEntity
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class QuizQuestionEntityTest {
    private lateinit var database: Database

    @BeforeTest
    fun setup() {
        database = DatabaseFactory.createTestDatabase()
    }

    @Test
    fun `findByEvent returns questions for the event and is empty for another`() {
        val eventId = QuizTestFixtures.createEvent(database)
        val otherEventId = QuizTestFixtures.createEvent(database)
        val partnerId = QuizTestFixtures.createPartner(database, eventId, quizCode = "ABCD")
        QuizTestFixtures.createQuestion(database, eventId, partnerId, "Q1", listOf("a", "b"), correctIndex = 0)

        transaction(database) {
            assertTrue(QuizQuestionEntity.findByEvent(eventId).empty().not())
            assertEquals(1, QuizQuestionEntity.findByEvent(eventId).count())
            assertTrue(QuizQuestionEntity.findByEvent(otherEventId).empty())
        }
    }
}
