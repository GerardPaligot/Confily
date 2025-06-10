package com.paligot.confily.backend.internals.helpers.date

sealed class FormatterPattern(val format: String) {
    object YearMonthDay : FormatterPattern("yyyy-MM-dd")
    object HoursMinutes : FormatterPattern("HH:mm")
    object SimplifiedIso : FormatterPattern("yyyy-MM-dd'T'HH:mm:ss")
    object Iso8601 : FormatterPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")
}
