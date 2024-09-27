package com.paligot.confily.core.database.mappers

import com.paligot.confily.models.Category
import com.paligot.confily.db.Category as CategoryDb

fun Category.convertToDb(eventId: String): CategoryDb = CategoryDb(
    id = id,
    name = name,
    color = color,
    icon = icon,
    selected = false,
    event_id = eventId
)
