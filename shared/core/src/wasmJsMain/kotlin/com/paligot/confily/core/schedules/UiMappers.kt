package com.paligot.confily.core.schedules

import com.paligot.confily.core.extensions.formatHoursMinutes
import com.paligot.confily.core.speakers.SpeakerDb
import com.paligot.confily.core.speakers.convertSpeakerItemUi
import com.paligot.confily.models.ui.AddressUi
import com.paligot.confily.models.ui.CategoryUi
import com.paligot.confily.models.ui.EventSessionItemUi
import com.paligot.confily.models.ui.FormatUi
import com.paligot.confily.models.ui.TalkItemUi
import com.paligot.confily.models.ui.TalkUi
import com.paligot.confily.resources.Strings
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

private const val MaxSpeakersCount = 3

fun SelectSessionsDb.convertTalkUi(
    speakers: List<SpeakerDb>,
    strings: Strings
): TalkUi {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val startTime = session.startTime.toLocalDateTime()
    val endTime = session.endTime.toLocalDateTime()
    val diff = endTime.toInstant(TimeZone.UTC).minus(startTime.toInstant(TimeZone.UTC))
    return TalkUi(
        title = talk.title,
        level = talk.level,
        abstract = talk.abstract,
        category = category.convertCategoryUi(),
        startTime = startTime.formatHoursMinutes(),
        endTime = endTime.formatHoursMinutes(),
        timeInMinutes = diff.inWholeMinutes.toInt(),
        room = session.room,
        speakers = speakers.map { it.convertSpeakerItemUi(strings) }.toImmutableList(),
        speakersSharing = speakers.joinToString(", ") { speaker ->
            speaker.twitter?.let { twitterName ->
                val username = twitterName.split("twitter.com/")[1]
                "${speaker.displayName} (@$username)"
            } ?: run { speaker.displayName }
        },
        canGiveFeedback = now > startTime,
        openFeedbackProjectId = null,
        openFeedbackSessionId = null,
        openFeedbackUrl = null
    )
}

fun SelectSessionsDb.convertTalkItemUi(
    speakers: List<SpeakerDb>,
    strings: Strings
): TalkItemUi {
    val startDateTime = session.startTime.toLocalDateTime()
    val endDateTime = session.endTime.toLocalDateTime()
    val diff = endDateTime.toInstant(TimeZone.UTC)
        .minus(startDateTime.toInstant(TimeZone.UTC))
    val timeInMinutes = diff.inWholeMinutes.toInt()
    val count = (speakers.size - MaxSpeakersCount).coerceAtLeast(minimumValue = 0)
    val maxSpeakers = speakers.take(MaxSpeakersCount)
    val speakersJoined = maxSpeakers.joinToString(", ") { it.displayName }
    val level = when (this.talk.level) {
        "advanced" -> strings.texts.levelAdvanced
        "intermediate" -> strings.texts.levelIntermediate
        "beginner" -> strings.texts.levelBeginner
        else -> talk.level
    }
    return TalkItemUi(
        id = talk.id,
        order = session.order.toInt(),
        title = talk.title,
        abstract = talk.abstract,
        room = session.room,
        level = level,
        slotTime = startDateTime.formatHoursMinutes(),
        startTime = session.startTime,
        endTime = session.endTime,
        timeInMinutes = timeInMinutes,
        time = strings.texts.scheduleMinutes(timeInMinutes),
        category = category.convertCategoryUi(),
        speakers = maxSpeakers.map { it.displayName }.toImmutableList(),
        speakersAvatar = maxSpeakers.map { it.photoUrl }.toImmutableList(),
        speakersLabel = strings.texts.speakersList(count, speakersJoined),
        isFavorite = session.isFavorite
    )
}

fun SelectEventSessionsDb.convertEventSessionItemUi(strings: Strings): EventSessionItemUi {
    val startDateTime = session.startTime.toLocalDateTime()
    val endDateTime = session.endTime.toLocalDateTime()
    val diff = endDateTime.toInstant(TimeZone.UTC)
        .minus(startDateTime.toInstant(TimeZone.UTC))
    val timeInMinutes = diff.inWholeMinutes.toInt()
    val address =
        if (event.formattedAddress != null && event.address != null && event.latitude != null && event.longitude != null) {
            AddressUi(
                event.formattedAddress.toImmutableList(),
                event.address,
                event.latitude,
                event.longitude
            )
        } else {
            null
        }
    return EventSessionItemUi(
        id = event.id,
        title = event.title,
        description = event.description,
        order = 0,
        room = session.room,
        slotTime = startDateTime.formatHoursMinutes(),
        startTime = session.startTime,
        endTime = session.endTime,
        timeInMinutes = timeInMinutes,
        time = strings.texts.scheduleMinutes(timeInMinutes),
        addressUi = address
    )
}

fun SelectTalksBySpeakerIdDb.convertTalkItemUi(
    session: SelectSessionsDb,
    speakers: List<SpeakerDb>,
    strings: Strings
): TalkItemUi {
    val startTime = session.session.startTime.toLocalDateTime()
    val endTime = session.session.endTime.toLocalDateTime()
    val diff = endTime.toInstant(TimeZone.UTC).minus(startTime.toInstant(TimeZone.UTC))
    val timeInMinutes = diff.inWholeMinutes.toInt()
    val count = (speakers.size - MaxSpeakersCount).coerceAtLeast(minimumValue = 0)
    val maxSpeakers = speakers.take(MaxSpeakersCount)
    val speakersJoined = maxSpeakers.joinToString(", ") { it.displayName }
    val level = when (session.talk.level) {
        "advanced" -> strings.texts.levelAdvanced
        "intermediate" -> strings.texts.levelIntermediate
        "beginner" -> strings.texts.levelBeginner
        else -> session.talk.level
    }
    return TalkItemUi(
        id = this.session.id,
        order = session.session.order.toInt(),
        title = this.session.title,
        abstract = this.session.abstract,
        room = session.session.room,
        level = this.session.level,
        slotTime = startTime.formatHoursMinutes(),
        startTime = session.session.startTime,
        endTime = session.session.endTime,
        timeInMinutes = timeInMinutes,
        time = strings.texts.scheduleMinutes(timeInMinutes),
        category = this.category.convertCategoryUi(),
        speakers = maxSpeakers.map { it.displayName }.toImmutableList(),
        speakersAvatar = maxSpeakers.map { it.photoUrl }.toImmutableList(),
        speakersLabel = strings.texts.speakersList(count, speakersJoined),
        isFavorite = session.session.isFavorite
    )
}

fun CategoryDb.convertCategoryUi() = CategoryUi(
    id = id,
    name = name,
    color = color,
    icon = icon
)

fun FormatDb.convertFormatUi() = FormatUi(
    id = id,
    name = name,
    time = time.toInt()
)
