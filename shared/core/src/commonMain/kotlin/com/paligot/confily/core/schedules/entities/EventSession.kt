package com.paligot.confily.core.schedules.entities

import com.paligot.confily.models.ui.EventSessionUi
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toInstant
import kotlin.native.ObjCName

@ObjCName("EventSessionEntity")
class EventSession(
    val id: String,
    val title: String,
    val description: String,
    val room: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val address: Address?
)

fun EventSession.mapToEventSessionUi(): EventSessionUi {
    val timeInMinutes = startTime.toInstant(TimeZone.UTC)
        .minus(endTime.toInstant(TimeZone.UTC))
    return EventSessionUi(
        title = title,
        description = description,
        room = room,
        slotTime = startTime.format(
            LocalDateTime.Format {
                hour()
                char(':')
                minute()
            }
        ),
        timeInMinutes = timeInMinutes.inWholeMinutes.toInt(),
        addressUi = address?.mapToAddressUi()
    )
}
