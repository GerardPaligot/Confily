package org.gdglille.devfest.database.mappers

import org.gdglille.devfest.models.Format
import org.gdglille.devfest.models.ui.FormatUi
import org.gdglille.devfest.db.Format as FormatDb

fun Format.convertToDb(eventId: String): FormatDb = FormatDb(
    id = id,
    name = name,
    time = time.toLong(),
    selected = false,
    event_id = eventId
)

fun FormatUi.convertToEntity(eventId: String, selected: Boolean) = FormatDb(
    id = id,
    name = name,
    time = time.toLong(),
    selected = selected,
    event_id = eventId
)
