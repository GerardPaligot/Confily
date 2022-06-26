package org.gdglille.devfest.backend.date

sealed class FormatterPattern(val format: String) {
    object HoursMinutes : FormatterPattern("HH:mm")
    object SimplifiedIso : FormatterPattern("yyyy-MM-dd'T'HH:mm:ss")
    object Iso8601 : FormatterPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")
}
