package org.gdglille.devfest.database.mappers

import org.gdglille.devfest.models.Category
import org.gdglille.devfest.db.Category as CategoryDb

fun Category.convertToDb(eventId: String): CategoryDb = CategoryDb(
    id = id,
    name = name,
    color = color,
    icon = icon,
    event_id = eventId
)
