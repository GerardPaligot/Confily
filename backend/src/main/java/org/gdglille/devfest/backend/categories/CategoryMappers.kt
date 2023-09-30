package org.gdglille.devfest.backend.categories

import org.gdglille.devfest.models.Category
import org.gdglille.devfest.models.inputs.CategoryInput

fun CategoryDb.convertToModel() = Category(
    id = id ?: "",
    name = name,
    color = color,
    icon = icon
)

fun CategoryInput.convertToDb(id: String? = null) = CategoryDb(
    id = id,
    name = name,
    color = color,
    icon = icon
)
