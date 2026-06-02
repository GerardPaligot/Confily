package com.paligot.confily.models.inputs

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuizPlayerInput(
    val username: String,
    @SerialName("device_id")
    val deviceId: String
) : Validator {
    override fun validate(): List<String> {
        val errors = mutableListOf<String>()
        if (username.isBlank()) errors.add("Username cannot be blank")
        if (deviceId.isBlank()) errors.add("Device id cannot be blank")
        return errors
    }
}

@Serializable
data class QuizAnswerInput(
    @SerialName("question_id")
    val questionId: String,
    @SerialName("answer_id")
    val answerId: String
) : Validator {
    override fun validate(): List<String> = emptyList()
}

@Serializable
data class QuizSubmissionInput(
    @SerialName("device_id")
    val deviceId: String,
    val answers: List<QuizAnswerInput>
) : Validator {
    override fun validate(): List<String> {
        val errors = mutableListOf<String>()
        if (deviceId.isBlank()) errors.add("Device id cannot be blank")
        if (answers.isEmpty()) errors.add("Answers cannot be empty")
        return errors
    }
}
