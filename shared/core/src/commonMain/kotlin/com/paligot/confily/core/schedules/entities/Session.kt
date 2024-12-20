package com.paligot.confily.core.schedules.entities

import com.paligot.confily.core.speakers.entities.SpeakerItem
import com.paligot.confily.core.speakers.entities.mapToSpeakerItemUi
import com.paligot.confily.models.ui.TalkUi
import com.paligot.confily.resources.Strings
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.native.ObjCName

@ObjCName("SessionEntity")
class Session(
    val id: String,
    val title: String,
    val abstract: String,
    val category: Category,
    val format: Format,
    val room: String,
    val level: Level?,
    val language: String?,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val speakers: List<SpeakerItem>,
    val feedback: FeedbackConfig?
)

fun Session.mapToTalkUi(strings: Strings): TalkUi {
    val now = Clock.System.now().toLocalDateTime(TimeZone.UTC)
    val diff = endTime.toInstant(TimeZone.UTC)
        .minus(startTime.toInstant(TimeZone.UTC))
    return TalkUi(
        title = title,
        level = level.mapToLocalizedString(strings),
        abstract = abstract,
        category = category.mapToCategoryUi(),
        slotTime = startTime.format(
            LocalDateTime.Format {
                hour()
                char(':')
                minute()
            }
        ),
        timeInMinutes = diff.inWholeMinutes.toInt(),
        room = room,
        speakers = speakers.map { it.mapToSpeakerItemUi(strings) }.toImmutableList(),
        speakersSharing = speakers.joinToString(", ") { it.displayName },
        canGiveFeedback = now > startTime,
        openFeedbackProjectId = feedback?.projectId,
        openFeedbackSessionId = feedback?.sessionId,
        openFeedbackUrl = feedback?.url
    )
}
