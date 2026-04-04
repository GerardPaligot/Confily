package com.paligot.confily.backend.sessions.domain

import com.paligot.confily.models.VoteAgendaSlot

interface SessionVoteRepository {
    suspend fun vote(eventId: String, sessionId: String)
    suspend fun sessionsSortedByVotes(eventId: String): Map<String, List<VoteAgendaSlot>>
}
