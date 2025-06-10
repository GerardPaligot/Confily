package com.paligot.confily.backend.speakers.domain

import com.paligot.confily.models.Speaker

interface SpeakerRepository {
    suspend fun list(eventId: String): List<Speaker>
}
