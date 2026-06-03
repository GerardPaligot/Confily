package com.paligot.confily.core.quiz

import com.paligot.confily.core.DeviceIdProvider
import com.paligot.confily.core.api.ConferenceApi
import com.paligot.confily.core.kvalue.ConferenceSettings
import com.paligot.confily.models.QuizQuestion
import com.paligot.confily.models.QuizSubmissionResult
import com.paligot.confily.models.inputs.QuizAnswerInput
import com.paligot.confily.models.inputs.QuizPlayerInput
import com.paligot.confily.models.inputs.QuizSubmissionInput
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class QuizRepositoryImpl(
    private val api: ConferenceApi,
    private val settings: ConferenceSettings,
    private val deviceIdProvider: DeviceIdProvider
) : QuizRepository {
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun register(username: String) {
        val eventId = settings.getEventId()
        api.registerQuizPlayer(
            eventId = eventId,
            input = QuizPlayerInput(username = username, deviceId = deviceId())
        )
        settings.putQuizUsername(username)
    }

    override fun storedUsername(): Flow<String?> = settings.fetchQuizUsername()

    override suspend fun questions(code: String): List<QuizQuestion> =
        api.fetchQuizQuestions(eventId = settings.getEventId(), code = code)

    override suspend fun submit(
        code: String,
        answers: List<QuizAnswerInput>
    ): QuizSubmissionResult {
        val result = api.submitQuiz(
            eventId = settings.getEventId(),
            code = code,
            input = QuizSubmissionInput(deviceId = deviceId(), answers = answers)
        )
        saveResult(code, result)
        return result
    }

    override fun savedResult(code: String): QuizSubmissionResult? = readResults()[code]

    override suspend fun cumulativeScore(): Int? {
        val username = settings.getQuizUsername() ?: return null
        return api.fetchQuizLeaderboard(settings.getEventId())
            .firstOrNull { it.username == username }
            ?.score
    }

    private fun deviceId(): String =
        settings.getQuizDeviceId() ?: deviceIdProvider.deviceId().also { settings.putQuizDeviceId(it) }

    private fun readResults(): Map<String, QuizSubmissionResult> =
        settings.getQuizResults()?.let {
            runCatching { json.decodeFromString<Map<String, QuizSubmissionResult>>(it) }.getOrNull()
        } ?: emptyMap()

    private fun saveResult(code: String, result: QuizSubmissionResult) {
        val updated = readResults() + (code to result)
        settings.putQuizResults(json.encodeToString(updated))
    }
}
