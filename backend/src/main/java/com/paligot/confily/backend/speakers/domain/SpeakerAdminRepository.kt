package com.paligot.confily.backend.speakers.domain

import com.paligot.confily.models.inputs.SpeakerInput

interface SpeakerAdminRepository {
    suspend fun create(eventId: String, speakerInput: SpeakerInput): String
    suspend fun update(eventId: String, speakerId: String, input: SpeakerInput): String
}
