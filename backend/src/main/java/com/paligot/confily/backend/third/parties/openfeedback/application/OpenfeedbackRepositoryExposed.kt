package com.paligot.confily.backend.third.parties.openfeedback.application

import com.paligot.confily.backend.schedules.infrastructure.exposed.ScheduleEntity
import com.paligot.confily.backend.schedules.infrastructure.exposed.SchedulesTable
import com.paligot.confily.backend.sessions.infrastructure.exposed.SessionSpeakersTable
import com.paligot.confily.backend.speakers.infrastructure.exposed.SpeakerEntity
import com.paligot.confily.backend.speakers.infrastructure.exposed.SpeakerSocialsTable
import com.paligot.confily.backend.speakers.infrastructure.exposed.SpeakersTable
import com.paligot.confily.backend.third.parties.openfeedback.domain.OpenfeedbackRepository
import com.paligot.confily.models.OpenFeedback
import com.paligot.confily.models.SessionOF
import com.paligot.confily.models.SocialOF
import com.paligot.confily.models.SpeakerOF
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class OpenfeedbackRepositoryExposed(
    private val database: Database
) : OpenfeedbackRepository {
    override suspend fun get(eventId: String): OpenFeedback = transaction(db = database) {
        val eventUuid = UUID.fromString(eventId)
        OpenFeedback(
            sessions = ScheduleEntity
                .findWithSession(eventUuid)
                .orderBy(SchedulesTable.displayOrder to SortOrder.ASC)
                .mapNotNull {
                    val session = it.session ?: return@mapNotNull null
                    val speakerIds = SessionSpeakersTable.speakerIds(session.id.value)
                    SessionOF(
                        id = session.id.value.toString(),
                        title = session.title,
                        trackTitle = it.eventSessionTrack.trackName,
                        speakers = speakerIds.map { it.toString() },
                        startTime = it.startTime.toLocalDateTime(TimeZone.UTC).toString(),
                        endTime = it.endTime.toLocalDateTime(TimeZone.UTC).toString(),
                        tags = emptyList()
                    )
                }
                .associateBy { it.id },
            speakers = SpeakerEntity
                .findByEvent(eventUuid)
                .orderBy(SpeakersTable.name to SortOrder.ASC)
                .map {
                    SpeakerOF(
                        id = it.id.value.toString(),
                        name = it.name,
                        photoUrl = it.photoUrl,
                        socials = SpeakerSocialsTable.socials(it.id.value).map { social ->
                            SocialOF(
                                name = social.type.name.lowercase(),
                                link = social.url
                            )
                        }
                    )
                }.associateBy { it.id }
        )
    }
}
