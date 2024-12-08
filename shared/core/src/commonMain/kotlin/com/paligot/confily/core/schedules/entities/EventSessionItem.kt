package com.paligot.confily.core.schedules.entities

import com.paligot.confily.models.ui.EventSessionItemUi
import com.paligot.confily.resources.Strings
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toInstant
import kotlin.native.ObjCName

@ObjCName("EventSessionItemEntity")
class EventSessionItem(
    id: String,
    order: Int,
    title: String,
    val description: String?,
    room: String,
    startTime: LocalDateTime,
    endTime: LocalDateTime
) : Item(id, order, title, room, startTime, endTime)

fun EventSessionItem.mapToEventSessionItemUi(strings: Strings): EventSessionItemUi {
    val diff = endTime.toInstant(TimeZone.UTC)
        .minus(startTime.toInstant(TimeZone.UTC))
    val timeInMinutes = diff.inWholeMinutes.toInt()
    return EventSessionItemUi(
        id = id,
        order = 0,
        title = title,
        room = room,
        slotTime = startTime.format(
            LocalDateTime.Format {
                hour()
                char(':')
                minute()
            }
        ),
        timeInMinutes = timeInMinutes,
        time = strings.texts.scheduleMinutes(timeInMinutes),
        isClickable = description != null
    )
}
