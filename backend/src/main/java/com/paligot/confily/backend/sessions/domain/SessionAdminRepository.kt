package com.paligot.confily.backend.sessions.domain

import com.paligot.confily.models.inputs.EventSessionInput
import com.paligot.confily.models.inputs.TalkSessionInput

interface SessionAdminRepository {
    suspend fun create(eventId: String, input: TalkSessionInput): String
    suspend fun update(eventId: String, talkId: String, input: TalkSessionInput): String
    suspend fun update(eventId: String, sessionId: String, input: EventSessionInput): String
}
