package org.gdglille.devfest.models

import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf

data class FiltersUi(
    val onlyFavorites: Boolean,
    val formats: ImmutableMap<FormatUi, Boolean>,
    val categories: ImmutableMap<CategoryUi, Boolean>
) {
    companion object {
        val fake = FiltersUi(
            onlyFavorites = false,
            formats = persistentMapOf(FormatUi.quickie to true, FormatUi.conference to false),
            categories = persistentMapOf(CategoryUi.fake to false)
        )
    }
}
