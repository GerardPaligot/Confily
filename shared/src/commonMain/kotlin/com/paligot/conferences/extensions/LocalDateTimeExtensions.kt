package com.paligot.conferences.extensions

import com.paligot.conferences.DecimalFormat
import kotlinx.datetime.LocalDateTime

fun LocalDateTime.formatHoursMinutes(): String {
    val formatter = DecimalFormat()
    return "${formatter.format(hour)}:${formatter.format(minute)}"
}