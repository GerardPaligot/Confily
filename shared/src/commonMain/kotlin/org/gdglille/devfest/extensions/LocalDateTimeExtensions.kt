package org.gdglille.devfest.extensions

import org.gdglille.devfest.DecimalFormat
import kotlinx.datetime.LocalDateTime

fun LocalDateTime.formatHoursMinutes(): String {
    val formatter = DecimalFormat()
    return "${formatter.format(hour)}:${formatter.format(minute)}"
}