package org.gdglille.devfest.database.mappers

import org.gdglille.devfest.models.Category
import org.gdglille.devfest.models.ui.CategoryUi
import org.gdglille.devfest.db.Category as CategoryDb

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
