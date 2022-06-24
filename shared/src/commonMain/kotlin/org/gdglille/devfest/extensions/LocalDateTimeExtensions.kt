package org.gdglille.devfest.extensions

import kotlinx.datetime.LocalDateTime
import org.gdglille.devfest.DecimalFormat

fun LocalDateTime.formatHoursMinutes(): String {
    val formatter = DecimalFormat()
    return "${formatter.format(hour)}:${formatter.format(minute)}"
}
