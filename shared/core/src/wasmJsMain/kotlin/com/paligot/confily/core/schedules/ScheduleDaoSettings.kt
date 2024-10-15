package com.paligot.confily.core.schedules

import cafe.adriel.lyricist.Lyricist
import com.paligot.confily.core.kvalue.ConferenceSettings
import com.paligot.confily.models.ui.AgendaUi
import com.paligot.confily.models.ui.CategoryUi
import com.paligot.confily.models.ui.EventSessionItemUi
import com.paligot.confily.models.ui.FiltersUi
import com.paligot.confily.models.ui.FormatUi
import com.paligot.confily.models.ui.TalkItemUi
import com.paligot.confily.models.ui.TalkUi
import com.paligot.confily.resources.Strings
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.datetime.toLocalDateTime

class ScheduleDaoSettings(
    private val settings: ConferenceSettings,
    private val sessionQueries: SessionQueries,
    private val categoryQueries: CategoryQueries,
    private val formatQueries: FormatQueries,
    private val lyricist: Lyricist<Strings>
) : ScheduleDao {
    override fun fetchSchedules(eventId: String): Flow<ImmutableMap<String, AgendaUi>> {
        return combine(
            flow = sessionQueries.selectSessions(eventId),
            flow2 = sessionQueries.selectBreakSessions(eventId),
            flow3 = categoryQueries.selectCategories(eventId),
            flow4 = formatQueries.selectFormats(eventId),
            flow5 = settings.fetchOnlyFavoritesFlag(),
            transform = { sessions, breaks, selectedCategories, selectedFormats, hasFavFilter ->
                val selectedCategoryIds = selectedCategories.map { it.id }
                val selectedFormatIds = selectedFormats.map { it.id }
                val hasCategoryFilter = selectedCategories.isNotEmpty()
                val hasFormatFilter = selectedFormats.isNotEmpty()
                val sessionItems = sessions
                    .filter {
                        val selectedCategory =
                            if (hasCategoryFilter) selectedCategoryIds.contains(it.category.id) else true
                        val selectedFormat =
                            if (hasFormatFilter) selectedFormatIds.contains(it.format.id) else true
                        val selectedFav = if (hasFavFilter) it.session.isFavorite else true
                        when {
                            hasCategoryFilter && hasFormatFilter.not() && hasFavFilter.not() -> selectedCategory
                            hasCategoryFilter && hasFormatFilter.not() && hasFavFilter -> selectedCategory && selectedFav
                            hasCategoryFilter.not() && hasFormatFilter && hasFavFilter.not() -> selectedFormat
                            hasCategoryFilter.not() && hasFormatFilter && hasFavFilter -> selectedFormat && selectedFav
                            hasCategoryFilter && hasFormatFilter && hasFavFilter.not() -> selectedCategory && selectedFormat
                            hasCategoryFilter.not() && hasFormatFilter.not() && hasFavFilter -> selectedFav
                            hasCategoryFilter && hasFormatFilter && hasFavFilter -> selectedCategory && selectedFormat && selectedFav
                            else -> true
                        }
                    }
                    .map {
                        val speakers = it.session.sessionTalkId?.let { talkId ->
                            sessionQueries
                                .getSpeakersByTalkId(eventId, talkId)
                        } ?: run { emptyList() }
                        it.convertTalkItemUi(speakers = speakers, strings = lyricist.strings)
                    } + breaks.map { it.convertEventSessionItemUi(lyricist.strings) }
                sessions.distinctBy { it.session.date }
                    .associate { session ->
                        session.session.date to AgendaUi(
                            onlyFavorites = hasFavFilter,
                            sessions = sessionItems
                                .filter { it.startTime.startsWith(session.session.date) }
                                .sortedBy { it.slotTime }
                                .groupBy { it.slotTime }
                                .mapValues { entry ->
                                    entry.value.sortedBy { it.order }.toImmutableList()
                                }
                                .toImmutableMap()
                        )
                    }
                    .toImmutableMap()
            }
        )
    }

    override fun fetchSchedule(eventId: String, talkId: String): Flow<TalkUi> = combine(
        flow = flowOf(sessionQueries.getSpeakersByTalkId(eventId, talkId)),
        flow2 = sessionQueries.selectSessionByTalkId(eventId, talkId),
        transform = { speakers, session ->
            session.convertTalkUi(speakers = speakers, strings = lyricist.strings)
        }
    )

    override fun fetchEventSession(eventId: String, sessionId: String): Flow<EventSessionItemUi> =
        sessionQueries.selectEventSessionById(eventId, sessionId)
            .map { it.convertEventSessionItemUi(lyricist.strings) }

    override fun fetchNextScheduleItems(
        eventId: String,
        date: String
    ): Flow<ImmutableList<TalkItemUi>> {
        val dateTime = date.toLocalDateTime()
        return sessionQueries
            .selectSessions(eventId)
            .map { sessions ->
                val nextAgenda = sessions
                    .filter { dateTime <= it.session.date.toLocalDateTime() }
                    .map {
                        val speakers = it.session.sessionTalkId?.let { talkId ->
                            sessionQueries.getSpeakersByTalkId(eventId, talkId)
                        } ?: run { emptyList() }
                        it.convertTalkItemUi(speakers = speakers, strings = lyricist.strings)
                    }
                    .groupBy { it.startTime }
                val toImmutableList = nextAgenda.values.first().toImmutableList()
                return@map toImmutableList
            }
    }

    override fun fetchFilters(eventId: String): Flow<FiltersUi> = combine(
        flow = categoryQueries.selectCategories(eventId),
        flow2 = formatQueries.selectFormats(eventId),
        flow3 = settings.fetchOnlyFavoritesFlag(),
        transform = { categories, formats, hasFavFilter ->
            FiltersUi(
                onlyFavorites = hasFavFilter,
                categories = categories
                    .associate { it.convertCategoryUi() to it.selected }
                    .toImmutableMap(),
                formats = formats
                    .associate { it.convertFormatUi() to it.selected }
                    .toImmutableMap()
            )
        }
    )

    override fun fetchFiltersAppliedCount(eventId: String): Flow<Int> = combine(
        flow = categoryQueries.selectSelectedCategories(eventId),
        flow2 = formatQueries.selectSelectedFormats(eventId),
        flow3 = settings.fetchOnlyFavoritesFlag(),
        transform = { selectedCategories, selectedFormats, hasFavFilter ->
            selectedCategories.size + selectedFormats.size + if (hasFavFilter) 1 else 0
        }
    )

    override fun applyFavoriteFilter(selected: Boolean) {
        settings.upsertOnlyFavoritesFlag(selected)
    }

    override fun applyCategoryFilter(categoryUi: CategoryUi, eventId: String, selected: Boolean) {
        categoryQueries.upsertCategory(categoryUi.convertToEntity(eventId, selected))
    }

    override fun applyFormatFilter(formatUi: FormatUi, eventId: String, selected: Boolean) {
        formatQueries.upsertFormat(formatUi.convertToEntity(eventId, selected))
    }

    override fun markAsFavorite(eventId: String, sessionId: String, isFavorite: Boolean) {
        sessionQueries.markAsFavorite(eventId, sessionId, isFavorite)
        if (isFavorite) return
        val onlyFavorites = settings.getOnlyFavoritesFlag()
        if (!onlyFavorites) return
        val countFavorites = sessionQueries.countSessionsByFavorite(eventId, true)
        if (countFavorites != null && countFavorites != 0) return
        settings.upsertOnlyFavoritesFlag(false)
    }
}
