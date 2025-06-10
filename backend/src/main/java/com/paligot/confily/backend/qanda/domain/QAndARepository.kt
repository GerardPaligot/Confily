package com.paligot.confily.backend.qanda.domain

import com.paligot.confily.models.QuestionAndResponse

interface QAndARepository {
    suspend fun list(eventId: String, language: String): List<QuestionAndResponse>
}
