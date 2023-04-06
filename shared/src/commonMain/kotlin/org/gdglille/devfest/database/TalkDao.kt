package org.gdglille.devfest.database

import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.gdglille.devfest.database.mappers.SpeakerMappers
import org.gdglille.devfest.db.Conferences4HallDatabase
import org.gdglille.devfest.extensions.formatHoursMinutes
import org.gdglille.devfest.models.CategoryUi
import org.gdglille.devfest.models.SpeakerItemUi
import org.gdglille.devfest.models.TalkUi

class TalkDao(private val db: Conferences4HallDatabase) {
    fun fetchTalk(eventId: String, talkId: String): TalkUi = db.talkQueries.transactionWithResult {
        val talkWithSpeakers = db.talkQueries
            .selectTalkWithSpeakersByTalkId(talkId, eventId)
            .executeAsList()
        val speakers = db.speakerQueries
            .selectSpeakers(talkWithSpeakers.map { it.speaker_id }, eventId)
            .executeAsList()
        val talk = db.talkQueries.selectTalk(talkId, eventId).executeAsOne()
        val openfeedbackProjectId = db.eventQueries.selectOpenfeedbackProjectId(eventId)
            .executeAsOne()
            .openfeedback_project_id
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val startTime = talk.start_time.toLocalDateTime()
        val endTime = talk.end_time.toLocalDateTime()
        val diff = endTime.toInstant(TimeZone.UTC).minus(startTime.toInstant(TimeZone.UTC))
        return@transactionWithResult TalkUi(
            title = talk.title,
            level = talk.level,
            abstract = talk.abstract_,
            category = CategoryUi(
                name = talk.category,
                color = talk.category_color,
                icon = talk.category_icon
            ),
            startTime = startTime.formatHoursMinutes(),
            endTime = endTime.formatHoursMinutes(),
            timeInMinutes = diff.inWholeMinutes.toInt(),
            room = talk.room,
            speakers = speakers.map {
                SpeakerItemUi(
                    id = it.id,
                    name = it.display_name,
                    pronouns = it.pronouns,
                    company = SpeakerMappers.displayActivity(it.job_title, it.company),
                    url = it.photo_url
                )
            }.toImmutableList(),
            speakersSharing = speakers.joinToString(", ") { speaker ->
                if (speaker.twitter == null) speaker.display_name
                else {
                    val twitter = speaker.twitter.split("twitter.com/").get(1)
                    "${speaker.display_name} (@$twitter)"
                }
            },
            canGiveFeedback = now > startTime,
            openFeedbackProjectId = openfeedbackProjectId,
            openFeedbackSessionId = talk.open_feedback,
            openFeedbackUrl = talk.open_feedback_url
        )
    }
}
