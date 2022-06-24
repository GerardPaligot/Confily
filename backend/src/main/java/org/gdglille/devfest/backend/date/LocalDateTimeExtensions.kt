package org.gdglille.devfest.backend.date

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

sealed class FormatterPattern(val format: String) {
    object HoursMinutes : FormatterPattern("HH:mm")
    object SimplifiedIso : FormatterPattern("yyyy-MM-dd'T'HH:mm:ss")
    object Iso8601 : FormatterPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")
}

fun LocalDateTime.format(pattern: FormatterPattern): String =
    format(DateTimeFormatter.ofPattern(pattern.format))