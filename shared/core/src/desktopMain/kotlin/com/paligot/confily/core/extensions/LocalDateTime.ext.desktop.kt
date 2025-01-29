package com.paligot.confily.core.extensions

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

actual fun LocalDateTime.formatLocalizedFull(): String = toJavaLocalDateTime()
    .toLocalDate()
    .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))
