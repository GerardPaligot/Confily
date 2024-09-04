package com.paligot.confily.core.database.mappers

import com.paligot.confily.models.Format
import com.paligot.confily.models.ui.FormatUi
import com.paligot.confily.db.Format as FormatDb

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
