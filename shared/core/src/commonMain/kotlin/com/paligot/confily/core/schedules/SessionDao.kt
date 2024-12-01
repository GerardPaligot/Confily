package com.paligot.confily.core.schedules

import com.paligot.confily.core.schedules.entities.Category
import com.paligot.confily.core.schedules.entities.EventSession
import com.paligot.confily.core.schedules.entities.EventSessionItem
import com.paligot.confily.core.schedules.entities.Format
import com.paligot.confily.core.schedules.entities.SelectableCategory
import com.paligot.confily.core.schedules.entities.SelectableFormat
import com.paligot.confily.core.schedules.entities.Session
import com.paligot.confily.core.schedules.entities.SessionItem
import com.paligot.confily.models.AgendaV4
import kotlinx.coroutines.flow.Flow

interface SessionDao {
    fun fetchSession(eventId: String, sessionId: String): Flow<Session>
    fun fetchEventSession(eventId: String, sessionId: String): Flow<EventSession>
    fun fetchSessionsFiltered(eventId: String): Flow<List<SessionItem>>
    fun fetchNextSessions(eventId: String, date: String): Flow<List<SessionItem>>
    fun fetchSessionsBySpeakerId(eventId: String, speakerId: String): Flow<List<SessionItem>>
    fun fetchEventSessions(eventId: String): Flow<List<EventSessionItem>>
    fun fetchCategories(eventId: String): Flow<List<SelectableCategory>>
    fun fetchSelectedCategories(eventId: String): Flow<List<Category>>
    fun fetchCountSelectedCategories(eventId: String): Flow<Long>
    fun fetchFormats(eventId: String): Flow<List<SelectableFormat>>
    fun fetchSelectedFormats(eventId: String): Flow<List<Format>>
    fun fetchCountSelectedFormats(eventId: String): Flow<Long>
    fun applyFavoriteFilter(selected: Boolean)
    fun applyCategoryFilter(eventId: String, categoryId: String, selected: Boolean)
    fun applyFormatFilter(eventId: String, formatId: String, selected: Boolean)
    fun markAsFavorite(eventId: String, sessionId: String, isFavorite: Boolean)
    fun insertAgenda(eventId: String, agenda: AgendaV4)
}
