package com.paligot.confily.core.schedules.entities

import com.paligot.confily.models.ui.FiltersUi
import kotlinx.collections.immutable.toImmutableMap
import kotlin.native.ObjCName

@ObjCName("FiltersEntity")
class Filters(
    val categories: List<SelectableCategory>,
    val formats: List<SelectableFormat>,
    val onlyFavorites: Boolean
)

@ObjCName("FiltersAppliedEntity")
class FiltersApplied(
    val categories: List<Category>,
    val formats: List<Format>,
    val onlyFavorites: Boolean
)

fun Filters.mapToUi() = FiltersUi(
    onlyFavorites = onlyFavorites,
    categories = categories.associate { it.mapToUi() to it.selected }.toImmutableMap(),
    formats = formats.associate { it.mapToUi() to it.selected }.toImmutableMap()
)
