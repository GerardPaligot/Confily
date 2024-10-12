package com.paligot.confily.core.schedules

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import cafe.adriel.lyricist.Lyricist
import com.paligot.confily.core.kvalue.ConferenceSettings
import com.paligot.confily.db.ConfilyDatabase
import com.paligot.confily.models.ui.AgendaUi
import com.paligot.confily.models.ui.CategoryUi
import com.paligot.confily.models.ui.EventSessionItemUi
import com.paligot.confily.models.ui.FiltersUi
import com.paligot.confily.models.ui.FormatUi
import com.paligot.confily.models.ui.TalkItemUi
import com.paligot.confily.models.ui.TalkUi
import com.paligot.confily.resources.Strings
import com.russhwolf.settings.ExperimentalSettingsApi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.datetime.toLocalDateTime
import kotlin.coroutines.CoroutineContext

@FlowPreview
@ExperimentalCoroutinesApi
@ExperimentalSettingsApi
class ScheduleDao(
    private val db: ConfilyDatabase,
    private val settings: ConferenceSettings,
    private val lyricist: Lyricist<Strings>,
    private val dispatcher: CoroutineContext
) {
    fun fetchSchedules(eventId: String): Flow<ImmutableMap<String, AgendaUi>> = combine(
        db.sessionQueries
            .selectSessions(eventId)
            .asFlow()
            .mapToList(dispatcher),
        db.sessionQueries
            .selectBreakSessions(eventId)
            .asFlow()
            .mapToList(dispatcher),
        db.categoryQueries
            .selectSelectedCategories(eventId)
            .asFlow()
            .mapToList(dispatcher),
        db.formatQueries
            .selectSelectedFormats(eventId)
            .asFlow()
            .mapToList(dispatcher),
        settings.fetchOnlyFavoritesFlag(),
        transform = { sessions, breaks, selectedCategories, selectedFormats, hasFavFilter ->
            val selectedCategoryIds = selectedCategories.map { it.id }
            val selectedFormatIds = selectedFormats.map { it.id }
            val hasCategoryFilter = selectedCategories.isNotEmpty()
            val hasFormatFilter = selectedFormats.isNotEmpty()
            val sessionItems = sessions
                .filter {
                    val selectedCategory =
                        if (hasCategoryFilter) selectedCategoryIds.contains(it.category_id) else true
                    val selectedFormat =
                        if (hasFormatFilter) selectedFormatIds.contains(it.format_id) else true
                    val selectedFav = if (hasFavFilter) it.is_favorite else true
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
                    val speakers = it.session_talk_id?.let { talkId ->
                        db.sessionQueries
                            .selectSpeakersByTalkId(eventId, talkId)
                            .executeAsList()
                    } ?: run { emptyList() }
                    it.convertTalkItemUi(speakers = speakers, strings = lyricist.strings)
                } + breaks.map { it.convertEventSessionItemUi(lyricist.strings) }
            sessions.distinctBy { it.date }
                .associate { session ->
                    session.date to AgendaUi(
                        onlyFavorites = hasFavFilter,
                        sessions = sessionItems
                            .filter { it.startTime.startsWith(session.date) }
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

    fun fetchSchedule(eventId: String, talkId: String): Flow<TalkUi> = db.transactionWithResult {
        combine(
            flow = db.sessionQueries
                .selectSpeakersByTalkId(event_id = eventId, talk_id = talkId)
                .asFlow()
                .mapToList(dispatcher),
            flow2 = db.eventQueries
                .selectOpenfeedbackProjectId(eventId)
                .asFlow()
                .mapToOne(dispatcher),
            flow3 = db.sessionQueries
                .selectSessionByTalkId(eventId, talkId)
                .asFlow()
                .mapToOne(dispatcher),
            transform = { speakers, openfeedback, talk ->
                talk.convertTalkUi(speakers, openfeedback, lyricist.strings)
            }
        )
    }

    fun fetchEventSession(eventId: String, sessionId: String): Flow<EventSessionItemUi> =
        db.sessionQueries.selectEventSessionById(event_id = eventId, session_event_id = sessionId)
            .asFlow()
            .mapToOne(dispatcher)
            .map { it.convertEventSessionItemUi(lyricist.strings) }

    fun fetchNextScheduleItems(eventId: String, date: String): Flow<ImmutableList<TalkItemUi>> {
        val dateTime = date.toLocalDateTime()
        return db.sessionQueries
            .selectSessions(eventId)
            .asFlow()
            .mapToList(dispatcher)
            .map { sessions ->
                val nextAgenda = sessions
                    .filter { dateTime < it.start_time.toLocalDateTime() }
                    .map {
                        val speakers = it.session_talk_id?.let { talkId ->
                            db.sessionQueries
                                .selectSpeakersByTalkId(eventId, talkId)
                                .executeAsList()
                        } ?: run { emptyList() }
                        it.convertTalkItemUi(speakers = speakers, strings = lyricist.strings)
                    }
                    .groupBy { it.startTime }
                val toImmutableList = nextAgenda.values.first().toImmutableList()
                return@map toImmutableList
            }
    }

    fun fetchFilters(eventId: String): Flow<FiltersUi> = combine(
        db.categoryQueries
            .selectCategories(eventId)
            .asFlow()
            .mapToList(dispatcher),
        db.formatQueries
            .selectFormats(eventId)
            .asFlow()
            .mapToList(dispatcher),
        settings.fetchOnlyFavoritesFlag(),
        transform = { categories, formats, onlyFavorites ->
            FiltersUi(
                onlyFavorites = onlyFavorites,
                categories = categories
                    .associate { it.convertCategoryUi() to it.selected }
                    .toImmutableMap(),
                formats = formats
                    .associate { it.convertFormatUi() to it.selected }
                    .toImmutableMap()
            )
        }
    )

    fun fetchFiltersAppliedCount(eventId: String): Flow<Int> = combine(
        db.categoryQueries
            .selectSelectedCategories(eventId)
            .asFlow()
            .mapToList(dispatcher),
        db.formatQueries
            .selectSelectedFormats(eventId)
            .asFlow()
            .mapToList(dispatcher),
        settings.fetchOnlyFavoritesFlag(),
        transform = { selectedCategories, selectedFormats, hasFavFilter ->
            return@combine selectedCategories.count() + selectedFormats.count() + if (hasFavFilter) 1 else 0
        }
    )

    fun applyFavoriteFilter(selected: Boolean) {
        settings.upsertOnlyFavoritesFlag(selected)
    }

    fun applyCategoryFilter(categoryUi: CategoryUi, eventId: String, selected: Boolean) =
        db.transaction {
            db.categoryQueries.upsertCategory(categoryUi.convertToEntity(eventId, selected))
        }

    fun applyFormatFilter(formatUi: FormatUi, eventId: String, selected: Boolean) =
        db.transaction {
            db.formatQueries.upsertFormat(formatUi.convertToEntity(eventId, selected))
        }

    fun markAsFavorite(eventId: String, sessionId: String, isFavorite: Boolean) = db.transaction {
        db.sessionQueries.markAsFavorite(
            is_favorite = isFavorite,
            id = sessionId,
            event_id = eventId
        )
        if (isFavorite) return@transaction
        val onlyFavorites = settings.getOnlyFavoritesFlag()
        if (!onlyFavorites) return@transaction
        val countFavorites = db.sessionQueries.countSessionsByFavorite(eventId, true)
            .executeAsOneOrNull()
        if (countFavorites != null && countFavorites != 0L) return@transaction
        settings.upsertOnlyFavoritesFlag(false)
    }
}
