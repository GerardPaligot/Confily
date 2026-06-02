package com.paligot.confily.core.quiz

import com.paligot.confily.core.api.QuizException
import com.paligot.confily.core.kvalue.ConferenceSettings
import com.paligot.confily.models.inputs.QuizAnswerInput
import com.russhwolf.settings.MapSettings
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

class QuizRepositoryTest {
    private fun settingsWithEvent(): ConferenceSettings =
        ConferenceSettings(MapSettings()).apply { insertEventId("event-1") }

    private val questionsJson = """
        [{"id":"q1","order":0,"question":"Capital of France?","options":[
          {"id":"a1","label":"Paris","order":0},{"id":"a2","label":"Lyon","order":1}]}]
    """.trimIndent()

    private val submissionResultJson = """
        {"correct_count":1,"total_count":1,"per_question":[
          {"question_id":"q1","chosen_answer_id":"a1","correct_answer_id":"a1","is_correct":true}]}
    """.trimIndent()

    @Test
    fun register_persists_username_and_posts_player() = runTest {
        val responder = RecordingResponder().apply { on("/quiz/players", HttpStatusCode.Created, """{"id":"p1"}""") }
        val settings = settingsWithEvent()
        val provider = FakeDeviceIdProvider("device-1")
        val repo = QuizRepository.Factory.create(conferenceApiWith(responder), settings, provider)

        repo.register("Alice")

        assertEquals("Alice", settings.getQuizUsername())
        assertEquals("device-1", settings.getQuizDeviceId())
        assertTrue(responder.requestedPaths.any { it.endsWith("/quiz/players") })
        assertTrue(responder.requestBodies.any { it.contains("Alice") && it.contains("device-1") })
    }

    @Test
    fun questions_returns_parsed_list() = runTest {
        val responder = RecordingResponder().apply { on("/quiz/partners/ABCD", HttpStatusCode.OK, questionsJson) }
        val repo = QuizRepository.Factory.create(conferenceApiWith(responder), settingsWithEvent(), FakeDeviceIdProvider())

        val questions = repo.questions("ABCD")

        assertEquals(1, questions.size)
        assertEquals("Capital of France?", questions[0].question)
        assertEquals(listOf("Paris", "Lyon"), questions[0].options.map { it.label })
    }

    @Test
    fun questions_maps_404_to_CodeNotFound() = runTest {
        val responder = RecordingResponder().apply { on("/quiz/partners/ZZZZ", HttpStatusCode.NotFound, "") }
        val repo = QuizRepository.Factory.create(conferenceApiWith(responder), settingsWithEvent(), FakeDeviceIdProvider())

        assertFailsWith<QuizException.CodeNotFound> { repo.questions("ZZZZ") }
    }

    @Test
    fun submit_returns_result_and_persists_it() = runTest {
        val responder = RecordingResponder().apply {
            on("/quiz/partners/ABCD/submit", HttpStatusCode.Created, submissionResultJson)
        }
        val settings = settingsWithEvent()
        val repo = QuizRepository.Factory.create(conferenceApiWith(responder), settings, FakeDeviceIdProvider())

        val result = repo.submit("ABCD", listOf(QuizAnswerInput("q1", "a1")))

        assertEquals(1, result.correctCount)
        assertEquals(1, result.totalCount)
        assertEquals(result.correctCount, repo.savedResult("ABCD")?.correctCount)
    }

    @Test
    fun submit_maps_409_to_AlreadySubmitted() = runTest {
        val responder = RecordingResponder().apply { on("/quiz/partners/ABCD/submit", HttpStatusCode.Conflict, "") }
        val repo = QuizRepository.Factory.create(conferenceApiWith(responder), settingsWithEvent(), FakeDeviceIdProvider())

        assertFailsWith<QuizException.AlreadySubmitted> { repo.submit("ABCD", listOf(QuizAnswerInput("q1", "a1"))) }
    }

    @Test
    fun submit_maps_404_to_PlayerNotRegistered() = runTest {
        val responder = RecordingResponder().apply { on("/quiz/partners/ABCD/submit", HttpStatusCode.NotFound, "") }
        val repo = QuizRepository.Factory.create(conferenceApiWith(responder), settingsWithEvent(), FakeDeviceIdProvider())

        assertFailsWith<QuizException.PlayerNotRegistered> { repo.submit("ABCD", listOf(QuizAnswerInput("q1", "a1"))) }
    }

    @Test
    fun savedResult_is_null_before_submission() = runTest {
        val repo = QuizRepository.Factory.create(conferenceApiWith(RecordingResponder()), settingsWithEvent(), FakeDeviceIdProvider())
        assertNull(repo.savedResult("ABCD"))
    }

    @Test
    fun cumulativeScore_matches_stored_username() = runTest {
        val responder = RecordingResponder().apply {
            on("/quiz/leaderboard", HttpStatusCode.OK, """[{"rank":1,"username":"Bob","score":9},{"rank":2,"username":"Alice","score":4}]""")
        }
        val settings = settingsWithEvent().apply { putQuizUsername("Alice") }
        val repo = QuizRepository.Factory.create(conferenceApiWith(responder), settings, FakeDeviceIdProvider())

        assertEquals(4, repo.cumulativeScore())
    }

    @Test
    fun cumulativeScore_is_null_when_user_not_in_leaderboard() = runTest {
        val responder = RecordingResponder().apply { on("/quiz/leaderboard", HttpStatusCode.OK, "[]") }
        val settings = settingsWithEvent().apply { putQuizUsername("Alice") }
        val repo = QuizRepository.Factory.create(conferenceApiWith(responder), settings, FakeDeviceIdProvider())

        assertNull(repo.cumulativeScore())
    }

    @Test
    fun cumulativeScore_is_null_when_no_username_stored() = runTest {
        val repo = QuizRepository.Factory.create(
            conferenceApiWith(RecordingResponder()),
            settingsWithEvent(),
            FakeDeviceIdProvider()
        )
        assertNull(repo.cumulativeScore())
    }

    @Test
    fun savedResult_returns_null_for_corrupt_stored_results() = runTest {
        val settings = settingsWithEvent().apply { putQuizResults("not-valid-json") }
        val repo = QuizRepository.Factory.create(conferenceApiWith(RecordingResponder()), settings, FakeDeviceIdProvider())
        assertNull(repo.savedResult("ABCD"))
    }

    @Test
    fun deviceId_is_reused_after_first_registration() = runTest {
        val responder = RecordingResponder().apply {
            on("/quiz/players", HttpStatusCode.Created, """{"id":"p1"}""")
            on("/quiz/partners/ABCD/submit", HttpStatusCode.Created, submissionResultJson)
        }
        val settings = settingsWithEvent()
        val provider = FakeDeviceIdProvider("device-1")
        val repo = QuizRepository.Factory.create(conferenceApiWith(responder), settings, provider)

        repo.register("Alice")
        repo.submit("ABCD", listOf(QuizAnswerInput("q1", "a1")))

        assertEquals(1, provider.callCount)
    }
}
