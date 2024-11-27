package com.paligot.confily.core.schedules

import com.paligot.confily.core.extensions.formatHoursMinutes
import com.paligot.confily.core.speakers.SpeakerDb
import com.paligot.confily.models.ui.CategoryUi
import com.paligot.confily.models.ui.EventSessionItemUi
import com.paligot.confily.models.ui.TalkItemUi
import com.paligot.confily.resources.Strings
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

private const val MaxSpeakersCount = 3

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
        room = session.room,
        level = level,
        slotTime = startDateTime.formatHoursMinutes(),
        startTime = session.startTime,
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
    return EventSessionItemUi(
        id = event.id,
        title = event.title,
        order = 0,
        room = session.room,
        slotTime = startDateTime.formatHoursMinutes(),
        timeInMinutes = timeInMinutes,
        time = strings.texts.scheduleMinutes(timeInMinutes),
        isClickable = event.description != null || event.address != null
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
    return TalkItemUi(
        id = this.session.id,
        order = session.session.order.toInt(),
        title = this.session.title,
        room = session.session.room,
        level = this.session.level,
        slotTime = startTime.formatHoursMinutes(),
        startTime = session.session.startTime,
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
