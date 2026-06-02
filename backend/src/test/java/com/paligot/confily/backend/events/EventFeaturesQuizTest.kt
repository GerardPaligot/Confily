package com.paligot.confily.backend.events

import com.paligot.confily.backend.addresses.infrastructure.provider.GeocodeApi
import com.paligot.confily.backend.events.application.EventRepositoryExposed
import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import com.paligot.confily.backend.events.infrastructure.exposed.EventFeatureEntity
import com.paligot.confily.backend.events.infrastructure.exposed.FeatureKey
import com.paligot.confily.backend.internals.infrastructure.exposed.DatabaseFactory
import com.paligot.confily.backend.quiz.QuizTestFixtures
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class EventFeaturesQuizTest {
    private lateinit var database: Database
    private lateinit var repository: EventRepositoryExposed

    @BeforeTest
    fun setup() {
        database = DatabaseFactory.createTestDatabase()
        repository = EventRepositoryExposed(database, mockk<GeocodeApi>(relaxed = true))
    }

    @Test
    fun `hasQuiz is false for an event with no quiz content`() = runBlocking {
        val eventId = QuizTestFixtures.createEvent(database)
        assertFalse(repository.getV4(eventId.toString()).features.hasQuiz)
    }

    @Test
    fun `hasQuiz is true when the event has at least one quiz question`() = runBlocking {
        val eventId = QuizTestFixtures.createEvent(database)
        val partnerId = QuizTestFixtures.createPartner(database, eventId, quizCode = "ABCD")
        QuizTestFixtures.createQuestion(database, eventId, partnerId, "Q1", listOf("a", "b"), correctIndex = 0)
        assertTrue(repository.getV4(eventId.toString()).features.hasQuiz)
    }

    @Test
    fun `hasQuiz is true when the Quiz feature flag is enabled even without questions`() = runBlocking {
        val eventId = QuizTestFixtures.createEvent(database)
        transaction(database) {
            EventFeatureEntity.new {
                this.event = EventEntity[eventId]
                this.featureKey = FeatureKey.Quiz
                this.enabled = true
            }
        }
        assertTrue(repository.getV4(eventId.toString()).features.hasQuiz)
    }
}
