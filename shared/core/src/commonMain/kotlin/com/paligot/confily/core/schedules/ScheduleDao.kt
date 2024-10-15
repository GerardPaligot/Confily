package com.paligot.confily.core.schedules

import com.paligot.confily.models.ui.AgendaUi
import com.paligot.confily.models.ui.CategoryUi
import com.paligot.confily.models.ui.EventSessionItemUi
import com.paligot.confily.models.ui.FiltersUi
import com.paligot.confily.models.ui.FormatUi
import com.paligot.confily.models.ui.TalkItemUi
import com.paligot.confily.models.ui.TalkUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.coroutines.flow.Flow

interface ScheduleDao {
    fun fetchSchedules(eventId: String): Flow<ImmutableMap<String, AgendaUi>>
    fun fetchSchedule(eventId: String, talkId: String): Flow<TalkUi>
    fun fetchEventSession(eventId: String, sessionId: String): Flow<EventSessionItemUi>
    fun fetchNextScheduleItems(eventId: String, date: String): Flow<ImmutableList<TalkItemUi>>
    fun fetchFilters(eventId: String): Flow<FiltersUi>
    fun fetchFiltersAppliedCount(eventId: String): Flow<Int>
    fun applyFavoriteFilter(selected: Boolean)
    fun applyCategoryFilter(categoryUi: CategoryUi, eventId: String, selected: Boolean)
    fun applyFormatFilter(formatUi: FormatUi, eventId: String, selected: Boolean)
    fun markAsFavorite(eventId: String, sessionId: String, isFavorite: Boolean)
}
