package com.paligot.confily.backend.categories

import com.paligot.confily.models.Category
import com.paligot.confily.models.inputs.CategoryInput

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
