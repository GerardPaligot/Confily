package com.paligot.confily.core.extensions

import com.paligot.confily.core.DecimalFormat
import kotlinx.datetime.LocalDateTime

fun LocalDateTime.formatHoursMinutes(): String {
    val formatter = DecimalFormat()
    return "${formatter.format(hour)}:${formatter.format(minute)}"
}

fun LocalDateTime.format(): String =
    "${this.dayOfWeek.name} ${this.dayOfMonth}, ${this.month.name} ${this.year}"

expect fun LocalDateTime.formatLocalizedFull(): String
