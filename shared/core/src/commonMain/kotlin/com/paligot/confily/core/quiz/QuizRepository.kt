package com.paligot.confily.core.quiz

import com.paligot.confily.core.DeviceIdProvider
import com.paligot.confily.core.api.ConferenceApi
import com.paligot.confily.core.kvalue.ConferenceSettings
import com.paligot.confily.models.QuizQuestion
import com.paligot.confily.models.QuizSubmissionResult
import com.paligot.confily.models.inputs.QuizAnswerInput
import kotlinx.coroutines.flow.Flow

interface QuizRepository {
    suspend fun register(username: String)
    fun storedUsername(): Flow<String?>
    suspend fun questions(code: String): List<QuizQuestion>
    suspend fun submit(code: String, answers: List<QuizAnswerInput>): QuizSubmissionResult
    fun savedResult(code: String): QuizSubmissionResult?
    suspend fun cumulativeScore(): Int?

    object Factory {
        fun create(
            api: ConferenceApi,
            settings: ConferenceSettings,
            deviceIdProvider: DeviceIdProvider
        ): QuizRepository = QuizRepositoryImpl(api, settings, deviceIdProvider)
    }
}
