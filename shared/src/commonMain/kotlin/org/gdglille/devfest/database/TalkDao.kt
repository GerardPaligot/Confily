package org.gdglille.devfest.database

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
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
                    company = it.company ?: "",
                    url = it.photo_url,
                    twitter = it.twitter?.split("twitter.com/")?.get(1)
                )
            },
            speakersSharing = speakers.joinToString(", ") { speaker ->
                if (speaker.twitter == null) speaker.display_name
                else {
                    val twitter = speaker.twitter.split("twitter.com/").get(1)
                    "${speaker.display_name} (@$twitter)"
                }
            },
            canGiveFeedback = now > startTime,
            openFeedbackSessionId = talk.open_feedback,
            openFeedbackUrl = talk.open_feedback_url
        )
    }
}
