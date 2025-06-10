package com.paligot.confily.backend.sessions.domain

import com.paligot.confily.models.Session

interface SessionRepository {
    suspend fun list(eventId: String): List<Session>
}
