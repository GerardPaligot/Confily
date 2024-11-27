package com.paligot.confily.core.schedules

import com.paligot.confily.core.kvalue.ConferenceSettings
import com.paligot.confily.core.schedules.entities.EventSession
import com.paligot.confily.core.schedules.entities.Filters
import com.paligot.confily.core.schedules.entities.FiltersApplied
import com.paligot.confily.core.schedules.entities.Session
import com.paligot.confily.core.schedules.entities.SessionItem
import com.paligot.confily.core.schedules.entities.Sessions
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    fun sessions(): Flow<Sessions>
    fun session(sessionId: String): Flow<Session>
    fun eventSession(sessionId: String): Flow<EventSession>
    fun fetchNextTalks(date: String): Flow<List<SessionItem>>
    fun filters(): Flow<Filters>
    fun filtersApplied(): Flow<FiltersApplied>
    fun countFilters(): Flow<Int>
    fun applyFavoriteFilter(selected: Boolean)
    fun applyCategoryFilter(categoryId: String, selected: Boolean)
    fun applyFormatFilter(formatId: String, selected: Boolean)
    fun markAsRead(sessionId: String, isFavorite: Boolean)

    object Factory {
        fun create(
            sessionDao: SessionDao,
            settings: ConferenceSettings
        ): SessionRepository = SessionRepositoryImpl(sessionDao, settings)
    }
}
