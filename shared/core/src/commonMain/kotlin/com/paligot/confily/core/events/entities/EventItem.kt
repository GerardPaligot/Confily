package com.paligot.confily.core.events.entities

import com.paligot.confily.models.ui.EventItemUi
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlin.native.ObjCName

@ObjCName("EventItemEntity")
class EventItem(
    val id: String,
    val name: String,
    val date: LocalDate,
    val past: Boolean
) {
    fun formattedDate(): String = date.format(
        LocalDate.Format {
            dayOfMonth(Padding.NONE)
            char('/')
            monthNumber(Padding.NONE)
            char('/')
            year()
        }
    )
}

fun EventItem.mapToUi(): EventItemUi = EventItemUi(
    id = id,
    name = name,
    date = formattedDate()
)
