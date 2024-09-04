package com.paligot.confily.core.database.mappers

import com.paligot.confily.models.Category
import com.paligot.confily.models.ui.CategoryUi
import com.paligot.confily.db.Category as CategoryDb

fun Category.convertToDb(eventId: String): CategoryDb = CategoryDb(
    id = id,
    name = name,
    color = color,
    icon = icon,
    selected = false,
    event_id = eventId
)

fun CategoryUi.convertToEntity(eventId: String, selected: Boolean) = CategoryDb(
    id = id,
    name = name,
    color = color ?: "",
    icon = icon ?: "",
    selected = selected,
    event_id = eventId
)
