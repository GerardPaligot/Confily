package com.paligot.confily.core.schedules.entities

import com.paligot.confily.models.ui.CategoryUi
import kotlin.native.ObjCName

@ObjCName("CategoryEntity")
open class Category(
    val id: String,
    val name: String,
    val color: String,
    val icon: String?
)

@ObjCName("SelectableCategoryEntity")
class SelectableCategory(
    id: String,
    name: String,
    color: String,
    icon: String?,
    val selected: Boolean
) : Category(id, name, color, icon)

fun Category.mapToCategoryUi() = CategoryUi(
    id = id,
    name = name,
    color = color,
    icon = icon
)

fun SelectableCategory.mapToCategoryUi() =
    CategoryUi(id = id, name = name, color = color, icon = icon)
