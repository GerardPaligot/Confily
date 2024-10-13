package com.paligot.confily.core.schedules

import com.paligot.confily.core.combineAllSerializableScopedFlow
import com.paligot.confily.core.getAllSerializableScopedFlow
import com.paligot.confily.core.getScopes
import com.paligot.confily.core.getSerializableScopedOrNull
import com.paligot.confily.core.putSerializableScoped
import com.paligot.confily.core.removeScope
import com.paligot.confily.core.schedules.CategoryQueries.Scopes.CATEGORIES
import com.russhwolf.settings.ObservableSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class CategoryQueries(private val settings: ObservableSettings) {
    private object Scopes {
        const val CATEGORIES = "categories"
    }

    fun selectCategories(eventId: String): Flow<List<CategoryDb>> = combine(
        flows = settings.getAllSerializableScopedFlow<CategoryDb>(CATEGORIES),
        transform = { categories ->
            categories
                .filter { it.eventId == eventId }
                .sortedBy { it.name }
        }
    )

    fun selectSelectedCategories(
        eventId: String,
        selected: Boolean = true
    ): Flow<List<CategoryDb>> = settings.combineAllSerializableScopedFlow(
        scope = CATEGORIES,
        filter = { it.eventId == eventId && it.selected == selected }
    )

    fun diffCategories(eventId: String, ids: List<String>): List<String> =
        settings.getScopes(CATEGORIES).filter { eventId == eventId && it !in ids }

    fun upsertCategory(category: CategoryDb) {
        settings.putSerializableScoped(CATEGORIES, category.id, category)
    }

    fun deleteCategories(ids: List<String>) {
        ids.forEach { settings.removeScope(CATEGORIES, it) }
    }

    fun getCategory(categoryId: String): CategoryDb? =
        settings.getSerializableScopedOrNull(CATEGORIES, categoryId)
}
