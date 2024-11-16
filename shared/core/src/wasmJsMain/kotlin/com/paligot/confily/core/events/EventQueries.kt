package com.paligot.confily.core.events

import com.paligot.confily.core.events.EventQueries.Scopes.EVENTS
import com.paligot.confily.core.events.EventQueries.Scopes.EVENT_ITEMS
import com.paligot.confily.core.getAllSerializableScopedFlow
import com.paligot.confily.core.getSerializableScopedFlow
import com.paligot.confily.core.putSerializableScoped
import com.russhwolf.settings.ObservableSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class EventQueries(private val settings: ObservableSettings) {
    private object Scopes {
        const val EVENTS = "events"
        const val EVENT_ITEMS = "eventItems"
    }

    fun insertEvent(eventDb: EventDb) {
        settings.putSerializableScoped(EVENTS, eventDb.id, eventDb)
    }

    fun selectEvent(id: String): Flow<EventDb> =
        settings.getSerializableScopedFlow<EventDb>(EVENTS, id)

    fun selectCoc(id: String): Flow<CocDb> = settings.getSerializableScopedFlow<EventDb>(EVENTS, id)
        .map {
            CocDb(
                url = it.cocUrl,
                coc = it.coc,
                email = it.contactEmail,
                phone = it.contactPhone
            )
        }

    fun insertEventItem(eventItemDb: EventItemDb) {
        settings.putSerializableScoped(EVENT_ITEMS, eventItemDb.id, eventItemDb)
    }

    fun selectEventItem(past: Boolean): Flow<List<EventItemDb>> = combine(
        flows = settings.getAllSerializableScopedFlow<EventItemDb>(EVENT_ITEMS),
        transform = { items ->
            items
                .filter { it.past == past }
                .sortedBy { it.timestamp }
        }
    )
}
