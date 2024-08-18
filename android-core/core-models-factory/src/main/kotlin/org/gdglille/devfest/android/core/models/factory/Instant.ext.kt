package org.gdglille.devfest.android.core.models.factory

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.toLocalDateTime

internal fun Instant.formatISO() = this
    .toLocalDateTime(TimeZone.currentSystemDefault())
    .format(LocalDateTime.Formats.ISO)
