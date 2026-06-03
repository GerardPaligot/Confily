package com.paligot.confily.core.quiz

import com.paligot.confily.models.QuizQuestion
import com.paligot.confily.models.QuizSubmissionResult
import com.paligot.confily.models.inputs.QuizAnswerInput
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import kotlinx.coroutines.flow.Flow

class QuizInteractor(private val repository: QuizRepository) {
    @NativeCoroutines
    suspend fun register(username: String): Unit = repository.register(username)

    @NativeCoroutines
    fun storedUsername(): Flow<String?> = repository.storedUsername()

    @NativeCoroutines
    suspend fun questions(code: String): List<QuizQuestion> = repository.questions(code)

    @NativeCoroutines
    suspend fun submit(code: String, answers: List<QuizAnswerInput>): QuizSubmissionResult =
        repository.submit(code, answers)

    fun savedResult(code: String): QuizSubmissionResult? = repository.savedResult(code)

    @NativeCoroutines
    suspend fun cumulativeScore(): Int? = repository.cumulativeScore()
}
