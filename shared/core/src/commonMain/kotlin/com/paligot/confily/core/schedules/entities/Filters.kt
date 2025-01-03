package com.paligot.confily.core.schedules.entities

import com.paligot.confily.schedules.ui.models.FiltersUi
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

fun Filters.mapToFiltersUi() = FiltersUi(
    onlyFavorites = onlyFavorites,
    categories = categories.associate { it.mapToCategoryUi() to it.selected }.toImmutableMap(),
    formats = formats.associate { it.mapToFormatUi() to it.selected }.toImmutableMap()
)
