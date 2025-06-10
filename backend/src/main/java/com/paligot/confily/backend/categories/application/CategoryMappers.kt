package com.paligot.confily.backend.categories.application

import com.paligot.confily.backend.internals.infrastructure.firestore.CategoryEntity
import com.paligot.confily.models.Category
import com.paligot.confily.models.inputs.CategoryInput

fun CategoryEntity.convertToModel() = Category(
    id = id ?: "",
    name = name,
    color = color,
    icon = icon
)

fun CategoryInput.convertToDb(id: String? = null) = CategoryEntity(
    id = id,
    name = name,
    color = color,
    icon = icon
)
