package com.paligot.confily.core.api

sealed class QuizException(message: String) : Exception(message) {
    class CodeNotFound(code: String) : QuizException("No quiz found for code $code")
    class AlreadySubmitted : QuizException("Answers already submitted for this quiz")
    class PlayerNotRegistered : QuizException("Player not registered for this device")
}
