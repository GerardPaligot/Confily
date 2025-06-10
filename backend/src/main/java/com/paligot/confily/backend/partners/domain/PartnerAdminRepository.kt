package com.paligot.confily.backend.partners.domain

import com.paligot.confily.models.inputs.PartnerInput

interface PartnerAdminRepository {
    suspend fun create(eventId: String, partnerInput: PartnerInput): String
    suspend fun update(eventId: String, partnerId: String, input: PartnerInput): String
}
