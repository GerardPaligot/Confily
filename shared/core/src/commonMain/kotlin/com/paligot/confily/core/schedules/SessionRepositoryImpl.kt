package com.paligot.confily.core.schedules

import com.paligot.confily.core.kvalue.ConferenceSettings
import com.paligot.confily.core.schedules.entities.EventSession
import com.paligot.confily.core.schedules.entities.Filters
import com.paligot.confily.core.schedules.entities.FiltersApplied
import com.paligot.confily.core.schedules.entities.Session
import com.paligot.confily.core.schedules.entities.SessionItem
import com.paligot.confily.core.schedules.entities.Sessions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat

@OptIn(ExperimentalCoroutinesApi::class)
class SessionRepositoryImpl(
    private val sessionDao: SessionDao,
    private val settings: ConferenceSettings
) : SessionRepository {
    override fun sessions(): Flow<Sessions> = settings.fetchEventId().flatMapConcat {
        combine(
            flow = sessionDao.fetchSessionsFiltered(it),
            flow2 = sessionDao.fetchEventSessions(it),
            flow3 = filtersApplied(),
            transform = { sessions, eventSessions, filters ->
                Sessions(
                    filtersApplied = filters,
                    sessions = (sessions + eventSessions)
                        .groupBy { it.startTime.date }
                        .map { entry ->
                            entry.key to entry.value
                                .sortedWith(compareBy({ it.startTime }, { it.order }))
                        }
                        .associate { it }
                )
            }
        )
    }

    override fun session(sessionId: String): Flow<Session> = settings.fetchEventId()
        .flatMapConcat { sessionDao.fetchSession(eventId = it, sessionId = sessionId) }

    override fun eventSession(sessionId: String): Flow<EventSession> =
        settings.fetchEventId()
            .flatMapConcat { sessionDao.fetchEventSession(eventId = it, sessionId = sessionId) }

    override fun fetchNextTalks(date: String): Flow<List<SessionItem>> =
        settings.fetchEventId()
            .flatMapConcat { sessionDao.fetchNextSessions(eventId = it, date = date) }

    override fun filters(): Flow<Filters> = settings.fetchEventId().flatMapConcat {
        combine(
            flow = sessionDao.fetchCategories(it),
            flow2 = sessionDao.fetchFormats(it),
            flow3 = settings.fetchOnlyFavoritesFlag(),
            transform = { categories, formats, onlyFavorites ->
                Filters(categories = categories, formats = formats, onlyFavorites = onlyFavorites)
            }
        )
    }

    override fun filtersApplied(): Flow<FiltersApplied> = settings.fetchEventId().flatMapConcat {
        combine(
            flow = sessionDao.fetchSelectedCategories(it),
            flow2 = sessionDao.fetchSelectedFormats(it),
            flow3 = settings.fetchOnlyFavoritesFlag(),
            transform = { categories, formats, onlyFavorites ->
                FiltersApplied(
                    categories = categories,
                    formats = formats,
                    onlyFavorites = onlyFavorites
                )
            }
        )
    }

    override fun countFilters(): Flow<Int> = settings.fetchEventId().flatMapConcat {
        combine(
            flow = sessionDao.fetchCountSelectedCategories(it),
            flow2 = sessionDao.fetchCountSelectedFormats(it),
            flow3 = settings.fetchOnlyFavoritesFlag(),
            transform = { countCategories, countFormats, hasFavFilter ->
                countCategories.toInt() + countFormats.toInt() + if (hasFavFilter) 1 else 0
            }
        )
    }

    override fun applyFavoriteFilter(selected: Boolean) = sessionDao.applyFavoriteFilter(selected)

    override fun applyCategoryFilter(categoryId: String, selected: Boolean) =
        sessionDao.applyCategoryFilter(settings.getEventId(), categoryId, selected)

    override fun applyFormatFilter(formatId: String, selected: Boolean) =
        sessionDao.applyFormatFilter(settings.getEventId(), formatId, selected)

    override fun markAsRead(sessionId: String, isFavorite: Boolean) = sessionDao.markAsFavorite(
        eventId = settings.getEventId(),
        sessionId = sessionId,
        isFavorite = isFavorite
    )
}
