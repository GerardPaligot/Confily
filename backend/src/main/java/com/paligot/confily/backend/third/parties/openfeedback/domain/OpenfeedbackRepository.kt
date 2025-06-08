package com.paligot.confily.backend.third.parties.openfeedback.domain

import com.paligot.confily.models.OpenFeedback

interface OpenfeedbackRepository {
    suspend fun get(eventId: String): OpenFeedback
}
