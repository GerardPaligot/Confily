package com.paligot.confily.core.database.mappers

import com.paligot.confily.models.Format
import com.paligot.confily.db.Format as FormatDb

fun Format.convertToDb(eventId: String): FormatDb = FormatDb(
    id = id,
    name = name,
    time = time.toLong(),
    selected = false,
    event_id = eventId
)
