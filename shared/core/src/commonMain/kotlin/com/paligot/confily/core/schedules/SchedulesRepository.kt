package com.paligot.confily.core.schedules

import com.paligot.confily.core.kvalue.ConferenceSettings
import com.paligot.confily.models.ui.AgendaUi
import com.paligot.confily.models.ui.CategoryUi
import com.paligot.confily.models.ui.EventSessionItemUi
import com.paligot.confily.models.ui.FiltersUi
import com.paligot.confily.models.ui.FormatUi
import com.paligot.confily.models.ui.TalkItemUi
import com.paligot.confily.models.ui.TalkUi
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map

interface SchedulesRepository {
    @NativeCoroutines
    fun agenda(): Flow<ImmutableMap<String, AgendaUi>>

    @NativeCoroutines
    fun scheduleItem(scheduleId: String): Flow<TalkUi>

    @NativeCoroutines
    fun scheduleEventSessionItem(scheduleId: String): Flow<EventSessionItemUi>

    @NativeCoroutines
    fun fetchNextTalks(date: String): Flow<ImmutableList<TalkItemUi>>

    @NativeCoroutines
    fun filters(): Flow<FiltersUi>

    @NativeCoroutines
    fun hasFilterApplied(): Flow<Boolean>

    fun applyFavoriteFilter(selected: Boolean)
    fun applyCategoryFilter(categoryUi: CategoryUi, selected: Boolean)
    fun applyFormatFilter(formatUi: FormatUi, selected: Boolean)
    fun markAsRead(sessionId: String, isFavorite: Boolean)

    object Factory {
        fun create(
            scheduleDao: ScheduleDao,
            settings: ConferenceSettings
        ): SchedulesRepository = SchedulesRepositoryImpl(scheduleDao, settings)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class SchedulesRepositoryImpl(
    private val scheduleDao: ScheduleDao,
    private val settings: ConferenceSettings
) : SchedulesRepository {
    override fun agenda(): Flow<ImmutableMap<String, AgendaUi>> = settings.fetchEventId()
        .flatMapConcat { scheduleDao.fetchSchedules(eventId = it) }

    override fun scheduleItem(scheduleId: String): Flow<TalkUi> = settings.fetchEventId()
        .flatMapConcat { scheduleDao.fetchSchedule(eventId = it, talkId = scheduleId) }

    override fun scheduleEventSessionItem(scheduleId: String): Flow<EventSessionItemUi> =
        settings.fetchEventId()
            .flatMapConcat { scheduleDao.fetchEventSession(eventId = it, sessionId = scheduleId) }

    override fun fetchNextTalks(date: String): Flow<ImmutableList<TalkItemUi>> =
        settings.fetchEventId()
            .flatMapConcat { scheduleDao.fetchNextScheduleItems(eventId = it, date = date) }

    override fun filters(): Flow<FiltersUi> = settings.fetchEventId()
        .flatMapConcat { scheduleDao.fetchFilters(it) }

    override fun hasFilterApplied(): Flow<Boolean> = settings.fetchEventId()
        .flatMapConcat { scheduleDao.fetchFiltersAppliedCount(eventId = it) }
        .map { it > 0 }

    override fun applyFavoriteFilter(selected: Boolean) = scheduleDao.applyFavoriteFilter(selected)

    override fun applyCategoryFilter(categoryUi: CategoryUi, selected: Boolean) =
        scheduleDao.applyCategoryFilter(categoryUi, settings.getEventId(), selected)

    override fun applyFormatFilter(formatUi: FormatUi, selected: Boolean) =
        scheduleDao.applyFormatFilter(formatUi, settings.getEventId(), selected)

    override fun markAsRead(sessionId: String, isFavorite: Boolean) = scheduleDao.markAsFavorite(
        eventId = settings.getEventId(),
        sessionId = sessionId,
        isFavorite = isFavorite
    )
}
