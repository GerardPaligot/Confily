package com.paligot.confily.backend.qanda.domain

import com.paligot.confily.models.inputs.QAndAInput

interface QAndAAdminRepository {
    suspend fun create(eventId: String, qAndA: QAndAInput): String
    suspend fun update(eventId: String, qandaId: String, input: QAndAInput): String
}
