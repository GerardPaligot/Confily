package com.paligot.confily.backend.quiz

import com.paligot.confily.backend.internals.infrastructure.exposed.DatabaseFactory
import com.paligot.confily.backend.quiz.application.QuizRepositoryExposed
import com.paligot.confily.backend.quiz.infrastructure.exposed.QuizPlayersTable
import com.paligot.confily.models.inputs.QuizPlayerInput
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
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
}
