package com.paligot.confily.backend.third.parties.partnersconnect.domain

import com.paligot.confily.backend.third.parties.partnersconnect.infrastructure.provider.PartnersConnectWebhookPayload

interface PartnersConnectRepository {
    suspend fun webhook(eventId: String, payload: PartnersConnectWebhookPayload): String
}
