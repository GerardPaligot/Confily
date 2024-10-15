package com.paligot.confily.core.schedules

import com.paligot.confily.core.combineAllSerializableScopedFlow
import com.paligot.confily.core.getAllSerializableScopedFlow
import com.paligot.confily.core.getScopes
import com.paligot.confily.core.getSerializableScopedOrNull
import com.paligot.confily.core.putSerializableScoped
import com.paligot.confily.core.removeScoped
import com.paligot.confily.core.schedules.FormatQueries.Scopes.FORMATS
import com.russhwolf.settings.ObservableSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class FormatQueries(private val settings: ObservableSettings) {
    private object Scopes {
        const val FORMATS = "formats"
    }

    fun selectFormats(eventId: String): Flow<List<FormatDb>> = combine(
        flows = settings.getAllSerializableScopedFlow<FormatDb>(FORMATS),
        transform = { formats ->
            formats
                .filter { it.eventId == eventId }
                .sortedBy { it.time }
        }
    )

    fun selectSelectedFormats(eventId: String, selected: Boolean = true): Flow<List<FormatDb>> =
        settings.combineAllSerializableScopedFlow(
            scope = FORMATS,
            filter = { it.eventId == eventId && it.selected == selected }
        )

    fun diffFormats(eventId: String, ids: List<String>): List<String> =
        settings.getScopes(FORMATS).filter { eventId == eventId && it !in ids }

    fun upsertFormat(format: FormatDb) {
        settings.putSerializableScoped(FORMATS, format.id, format)
    }

    fun deleteFormats(ids: List<String>) {
        ids.forEach { settings.removeScoped(FORMATS, it) }
    }

    fun getFormat(formatId: String): FormatDb? =
        settings.getSerializableScopedOrNull(FORMATS, formatId)
}
