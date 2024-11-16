package com.paligot.confily.core.extensions

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format

actual fun LocalDateTime.formatLocalizedFull(): String = this.format(LocalDateTime.Formats.ISO)
