package org.gdglille.devfest.backend.internals.date

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun LocalDateTime.format(pattern: FormatterPattern): String =
    format(DateTimeFormatter.ofPattern(pattern.format))
