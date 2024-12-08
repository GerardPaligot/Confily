package com.paligot.confily.core.schedules.entities

import com.paligot.confily.core.speakers.entities.SpeakerItem
import com.paligot.confily.models.ui.TalkItemUi
import com.paligot.confily.resources.Strings
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toInstant
import kotlin.native.ObjCName

@ObjCName("SessionItemEntity")
class SessionItem(
    id: String,
    order: Int,
    title: String,
    val category: Category,
    val format: Format,
    val level: Level?,
    room: String,
    val language: String?,
    startTime: LocalDateTime,
    endTime: LocalDateTime,
    val speakers: List<SpeakerItem>,
    val isFavorite: Boolean
) : Item(id, order, title, room, startTime, endTime)

private const val MaxSpeakersCount = 3

fun SessionItem.mapToTalkItemUi(strings: Strings): TalkItemUi {
    val diff = endTime.toInstant(TimeZone.UTC)
        .minus(startTime.toInstant(TimeZone.UTC))
    val timeInMinutes = diff.inWholeMinutes.toInt()
    val count = (speakers.size - MaxSpeakersCount).coerceAtLeast(minimumValue = 0)
    val maxSpeakers = speakers.take(MaxSpeakersCount)
    val speakersJoined = maxSpeakers.joinToString(", ") { it.displayName }
    return TalkItemUi(
        id = id,
        order = order,
        title = title,
        room = room,
        level = level.mapToLocalizedString(strings),
        slotTime = startTime.format(
            LocalDateTime.Format {
                hour()
                char(':')
                minute()
            }
        ),
        startTime = startTime.format(LocalDateTime.Formats.ISO),
        timeInMinutes = timeInMinutes,
        time = strings.texts.scheduleMinutes(timeInMinutes),
        category = category.mapToCategoryUi(),
        speakers = maxSpeakers.map { it.displayName }.toImmutableList(),
        speakersAvatar = maxSpeakers.map { it.photoUrl }.toImmutableList(),
        speakersLabel = strings.texts.speakersList(count, speakersJoined),
        isFavorite = isFavorite
    )
}
