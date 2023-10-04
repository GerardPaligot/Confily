package org.gdglille.devfest.database.mappers

import org.gdglille.devfest.models.Format
import org.gdglille.devfest.db.Format as FormatDb

fun Format.convertToDb(eventId: String): FormatDb = FormatDb(
    id = id,
    name = name,
    time = time.toLong(),
    event_id = eventId
)
