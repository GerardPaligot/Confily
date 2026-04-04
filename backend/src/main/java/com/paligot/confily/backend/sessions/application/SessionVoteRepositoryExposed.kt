package com.paligot.confily.backend.sessions.application

import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import com.paligot.confily.backend.schedules.infrastructure.exposed.ScheduleEntity
import com.paligot.confily.backend.sessions.domain.SessionVoteRepository
import com.paligot.confily.backend.sessions.infrastructure.exposed.SessionEntity
import com.paligot.confily.backend.sessions.infrastructure.exposed.SessionSpeakersTable
import com.paligot.confily.backend.sessions.infrastructure.exposed.SessionVotesTable
import com.paligot.confily.backend.speakers.infrastructure.exposed.SpeakerEntity
import com.paligot.confily.models.SessionVoteStats
import com.paligot.confily.models.VoteAgendaSlot
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class SessionVoteRepositoryExposed(
    private val database: Database
) : SessionVoteRepository {

    override suspend fun vote(eventId: String, sessionId: String) {
        val eventUuid = UUID.fromString(eventId)
        val sessionUuid = UUID.fromString(sessionId)
        transaction(db = database) {
            SessionEntity.findById(eventUuid, sessionUuid)
                ?: throw NotFoundException("Session $sessionId not found for event $eventId")
            SessionVotesTable.insert {
                it[SessionVotesTable.eventId] = eventUuid
                it[SessionVotesTable.sessionId] = sessionUuid
            }
        }
    }

    override suspend fun sessionsSortedByVotes(eventId: String): Map<String, List<VoteAgendaSlot>> {
        val eventUuid = UUID.fromString(eventId)
        return transaction(db = database) {
            val timezone = TimeZone.of(EventEntity[eventUuid].timezone)
            val votesBySession = SessionVotesTable
                .selectAll()
                .where { SessionVotesTable.eventId eq eventUuid }
                .groupBy { it[SessionVotesTable.sessionId].value }
                .mapValues { (_, rows) -> rows.size.toLong() }
            ScheduleEntity
                .findWithSession(eventUuid)
                .groupBy { it.startTime.toLocalDateTime(timezone).date.toString() }
                .mapValues { (_, schedulesByDay) ->
                    schedulesByDay
                        .groupBy { it.startTime.toLocalDateTime(timezone) }
                        .entries
                        .sortedBy { it.key }
                        .map { (_, schedulesInSlot) ->
                            val first = schedulesInSlot.first()
                            VoteAgendaSlot(
                                startTime = first.startTime.toLocalDateTime(timezone).toString(),
                                endTime = first.endTime.toLocalDateTime(timezone).toString(),
                                sessions = schedulesInSlot.mapNotNull { schedule ->
                                    val session = schedule.session ?: return@mapNotNull null
                                    val speakers = SessionSpeakersTable.speakers(session.id.value)
                                        .map { speakerId -> SpeakerEntity[speakerId].name }
                                    SessionVoteStats(
                                        id = session.id.value.toString(),
                                        title = session.title,
                                        language = session.language,
                                        level = session.level,
                                        format = session.format?.name,
                                        speakers = speakers,
                                        voteCount = votesBySession[session.id.value] ?: 0L
                                    )
                                }.sortedByDescending { it.voteCount }
                            )
                        }
                }
                .toSortedMap()
        }
    }
}
