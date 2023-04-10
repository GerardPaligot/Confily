package org.gdglille.devfest.database.mappers

import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.gdglille.devfest.extensions.formatHoursMinutes
import org.gdglille.devfest.models.CategoryUi
import org.gdglille.devfest.models.TalkItemUi

object TalkMappers {
    val talkItem = { id: String, order: Long, _: String, startTime: String, endTime: String, room: String,
        title: String, abstract: String, category: String, categoryColor: String?, categoryIcon: String?,
        speakers: List<String>, speakersAvatar: List<String>, is_favorite: Boolean, level: String? ->
        val startDateTime = startTime.toLocalDateTime()
        val endDateTime = endTime.toLocalDateTime()
        val diff = endDateTime.toInstant(TimeZone.UTC).minus(startDateTime.toInstant(TimeZone.UTC))
        TalkItemUi(
            id = id,
            order = order.toInt(),
            room = room,
            level = level,
            slotTime = startDateTime.formatHoursMinutes(),
            startTime = startTime,
            endTime = endTime,
            timeInMinutes = diff.inWholeMinutes.toInt(),
            title = title,
            abstract = abstract,
            category = CategoryUi(name = category, color = categoryColor, icon = categoryIcon),
            speakers = speakers.toImmutableList(),
            speakersAvatar = speakersAvatar.toImmutableList(),
            isFavorite = is_favorite
        )
    }
}
