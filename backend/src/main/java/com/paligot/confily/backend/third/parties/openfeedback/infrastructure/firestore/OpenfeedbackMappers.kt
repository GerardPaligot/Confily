package com.paligot.confily.backend.third.parties.openfeedback.infrastructure.firestore

import com.paligot.confily.backend.internals.helpers.date.FormatterPattern
import com.paligot.confily.backend.internals.helpers.date.format
import com.paligot.confily.models.AgendaV4
import com.paligot.confily.models.OpenFeedback
import com.paligot.confily.models.Session
import com.paligot.confily.models.SessionOF
import com.paligot.confily.models.SocialOF
import com.paligot.confily.models.SpeakerOF
import java.time.LocalDateTime

fun AgendaV4.convertToOpenFeedback(): OpenFeedback {
    val sessions = this.sessions
        .filterIsInstance<Session.Talk>()
        .mapNotNull { session ->
            val schedule = this.schedules.find { it.sessionId == session.id } ?: return@mapNotNull null
            val startTime = LocalDateTime.parse(schedule.startTime).format(FormatterPattern.SimplifiedIso)
            val endTime = LocalDateTime.parse(schedule.endTime).format(FormatterPattern.SimplifiedIso)
            SessionOF(
                id = session.id,
                title = session.title,
                trackTitle = schedule.room,
                speakers = session.speakers,
                startTime = "$startTime+02:00",
                endTime = "$endTime+02:00",
                tags = this.categories.find { it.id == session.categoryId }?.let { listOf(it.name) } ?: emptyList()
            )
        }
    val speakers = this.speakers
        .map {
            SpeakerOF(
                id = it.id,
                name = it.displayName,
                photoUrl = it.photoUrl,
                socials = it.socials.map { SocialOF(name = it.type.name.lowercase(), link = it.url) }
            )
        }
    return OpenFeedback(
        sessions = sessions.associateBy { it.id },
        speakers = speakers.associateBy { it.id }
    )
}
