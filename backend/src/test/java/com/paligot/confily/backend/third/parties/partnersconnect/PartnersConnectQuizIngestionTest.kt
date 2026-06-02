package com.paligot.confily.backend.third.parties.partnersconnect

import com.paligot.confily.backend.addresses.infrastructure.provider.GeocodeApi
import com.paligot.confily.backend.internals.infrastructure.exposed.DatabaseFactory
import com.paligot.confily.backend.partners.infrastructure.exposed.PartnerEntity
import com.paligot.confily.backend.quiz.infrastructure.exposed.QuizAnswerEntity
import com.paligot.confily.backend.quiz.infrastructure.exposed.QuizAnswersTable
import com.paligot.confily.backend.quiz.infrastructure.exposed.QuizQuestionEntity
import com.paligot.confily.backend.quiz.infrastructure.exposed.QuizQuestionsTable
import com.paligot.confily.backend.third.parties.partnersconnect.application.PartnersConnectRepositoryExposed
import com.paligot.confily.backend.third.parties.partnersconnect.infrastructure.provider.Company
import com.paligot.confily.backend.third.parties.partnersconnect.infrastructure.provider.CompanyStatus
import com.paligot.confily.backend.third.parties.partnersconnect.infrastructure.provider.EventSummary
import com.paligot.confily.backend.third.parties.partnersconnect.infrastructure.provider.Media
import com.paligot.confily.backend.third.parties.partnersconnect.infrastructure.provider.PartnersConnectWebhookPayload
import com.paligot.confily.backend.third.parties.partnersconnect.infrastructure.provider.PartnershipDetail
import com.paligot.confily.backend.third.parties.partnersconnect.infrastructure.provider.PartnershipPack
import com.paligot.confily.backend.third.parties.partnersconnect.infrastructure.provider.PartnershipProcessStatus
import com.paligot.confily.backend.third.parties.partnersconnect.infrastructure.provider.WebhookAnswer
import com.paligot.confily.backend.third.parties.partnersconnect.infrastructure.provider.WebhookEventType
import com.paligot.confily.backend.third.parties.partnersconnect.infrastructure.provider.WebhookQuestion
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class PartnersConnectQuizIngestionTest {
    private lateinit var database: Database
    private lateinit var repository: PartnersConnectRepositoryExposed
    private lateinit var eventId: UUID

    @BeforeTest
    fun setup() {
        database = DatabaseFactory.createTestDatabase()
        repository = PartnersConnectRepositoryExposed(database, mockk<GeocodeApi>())
        eventId = com.paligot.confily.backend.quiz.QuizTestFixtures.createEvent(database)
    }

    private fun payload(questions: List<WebhookQuestion>) = PartnersConnectWebhookPayload(
        eventType = WebhookEventType.CREATED,
        partnership = PartnershipDetail(
            id = "partnership-1",
            language = "en",
            validatedPack = PartnershipPack(id = "pack-1", name = "Gold"),
            processStatus = PartnershipProcessStatus(billingStatus = "paid"),
            createdAt = "2026-01-01T00:00:00"
        ),
        company = Company(
            id = "company-1",
            name = "ACME",
            headOffice = null,
            siret = null,
            vat = null,
            description = "desc",
            siteUrl = "https://acme.test",
            medias = Media(
                original = "https://acme.test/logo.svg",
                png1000 = "https://acme.test/1000.png",
                png500 = "https://acme.test/500.png",
                png250 = "https://acme.test/250.png"
            ),
            status = CompanyStatus.ACTIVE,
            socials = emptyList()
        ),
        event = EventSummary(slug = "test-event", name = "Test Event"),
        jobs = emptyList(),
        activities = emptyList(),
        speakers = emptyList(),
        supportVideoUrl = null,
        timestamp = "2026-01-01T00:00:00",
        questions = questions
    )

    @Test
    fun `webhook ingests questions, answers and assigns a 4-letter quiz code`() = runBlocking {
        val partnerId = UUID.fromString(
            repository.webhook(
                eventId.toString(),
                payload(
                    listOf(
                        WebhookQuestion(
                            id = "q1",
                            question = "Capital of France?",
                            order = 0,
                            answers = listOf(
                                WebhookAnswer(id = "a1", answer = "Paris", isCorrect = true, order = 0),
                                WebhookAnswer(id = "a2", answer = "Lyon", isCorrect = false, order = 1)
                            )
                        )
                    )
                )
            )
        )

        transaction(database) {
            val partner = PartnerEntity[partnerId]
            assertNotNull(partner.quizCode)
            assertEquals(4, partner.quizCode!!.length)

            val questions = QuizQuestionEntity.findByPartner(partnerId)
                .orderBy(QuizQuestionsTable.displayOrder to SortOrder.ASC)
                .toList()
            assertEquals(1, questions.size)
            assertEquals("Capital of France?", questions[0].question)

            val answers = QuizAnswerEntity.findByQuestion(questions[0].id.value)
                .orderBy(QuizAnswersTable.displayOrder to SortOrder.ASC)
                .toList()
            assertEquals(listOf("Paris", "Lyon"), answers.map { it.answer })
            assertTrue(answers[0].isCorrect)
            assertTrue(!answers[1].isCorrect)
        }
    }

    @Test
    fun `webhook re-sync updates answers in place and removes answers no longer present`() = runBlocking {
        val partnerId = UUID.fromString(
            repository.webhook(
                eventId.toString(),
                payload(
                    listOf(
                        WebhookQuestion(
                            id = "q1",
                            question = "Q1",
                            order = 0,
                            answers = listOf(
                                WebhookAnswer("a1", "Paris", true, 0),
                                WebhookAnswer("a2", "Lyon", false, 1)
                            )
                        )
                    )
                )
            )
        )
        val originalAnswerIds = transaction(database) {
            val question = QuizQuestionEntity.findByPartner(partnerId).toList().single()
            QuizAnswerEntity.findByQuestion(question.id.value).associate { it.externalId to it.id.value }
        }

        repository.webhook(
            eventId.toString(),
            payload(
                listOf(
                    WebhookQuestion(
                        id = "q1",
                        question = "Q1",
                        order = 0,
                        answers = listOf(
                            WebhookAnswer("a1", "Paris (updated)", true, 0),
                            WebhookAnswer("a3", "Nice", false, 1)
                        )
                    )
                )
            )
        )

        transaction(database) {
            val question = QuizQuestionEntity.findByPartner(partnerId).toList().single()
            val answers = QuizAnswerEntity.findByQuestion(question.id.value)
                .orderBy(QuizAnswersTable.displayOrder to SortOrder.ASC)
                .toList()
            assertEquals(listOf("a1", "a3"), answers.map { it.externalId })
            assertEquals(listOf("Paris (updated)", "Nice"), answers.map { it.answer })
            // a1 keeps its primary key, so any submission row referencing it survives the re-sync.
            assertEquals(originalAnswerIds["a1"], answers.first { it.externalId == "a1" }.id.value)
        }
    }

    @Test
    fun `webhook re-sync removes questions no longer present`() = runBlocking {
        val firstPartnerId = UUID.fromString(
            repository.webhook(
                eventId.toString(),
                payload(
                    listOf(
                        WebhookQuestion("q1", "Q1", 0, listOf(WebhookAnswer("a1", "A", true, 0))),
                        WebhookQuestion("q2", "Q2", 1, listOf(WebhookAnswer("a2", "B", true, 0)))
                    )
                )
            )
        )
        repository.webhook(
            eventId.toString(),
            payload(listOf(WebhookQuestion("q1", "Q1 updated", 0, listOf(WebhookAnswer("a1", "A", true, 0)))))
        )

        transaction(database) {
            val questions = QuizQuestionEntity.findByPartner(firstPartnerId).toList()
            assertEquals(1, questions.size)
            assertEquals("Q1 updated", questions[0].question)
        }
    }
}
