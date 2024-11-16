package com.paligot.confily.core.extensions

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.toInstant
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSISO8601DateFormatter

actual fun LocalDateTime.formatLocalizedFull(): String {
    val dateInstant = this.toInstant(TimeZone.currentSystemDefault())
        .format(DateTimeComponents.Formats.ISO_DATE_TIME_OFFSET)
    val formatter = NSDateFormatter()
    formatter.dateFormat = "EEEE, MMM d, yyyy"
    return formatter.stringFromDate(NSISO8601DateFormatter().dateFromString(dateInstant)!!)
}
