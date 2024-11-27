package com.paligot.confily.core.schedules.entities

import com.paligot.confily.models.ui.SessionItemUi
import com.paligot.confily.resources.Strings
import kotlinx.datetime.LocalDateTime

sealed class Item(
    open val id: String,
    open val order: Int,
    open val title: String,
    open val room: String,
    open val startTime: LocalDateTime,
    open val endTime: LocalDateTime
)

fun Item.mapToUi(strings: Strings): SessionItemUi = when (this) {
    is SessionItem -> mapToUi(strings)
    is EventSessionItem -> mapToUi(strings)
}
