package com.paligot.confily.schedules.ui.models

data class EventSessionUi(
    val title: String,
    val description: String,
    val room: String,
    val slotTime: String,
    val timeInMinutes: Int,
    val addressUi: AddressUi?
)
