package com.paligot.confily.core.models.factory

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.toLocalDateTime

internal fun Instant.formatDateISO() = this
    .toLocalDateTime(TimeZone.UTC)
    .date
    .format(LocalDate.Formats.ISO)

internal fun Instant.formatISO() = this
    .toLocalDateTime(TimeZone.UTC)
    .format(LocalDateTime.Formats.ISO)
