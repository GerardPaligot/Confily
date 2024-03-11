package org.gdglille.devfest.database

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.getBooleanFlow
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.gdglille.devfest.database.mappers.convertCategoryUi
import org.gdglille.devfest.database.mappers.convertFormatUi
import org.gdglille.devfest.database.mappers.convertTalkItemUi
import org.gdglille.devfest.database.mappers.convertToDb
import org.gdglille.devfest.database.mappers.convertToEntity
import org.gdglille.devfest.db.Conferences4HallDatabase
import org.gdglille.devfest.models.AgendaV3
import org.gdglille.devfest.models.ui.AgendaUi
import org.gdglille.devfest.models.ui.CategoryUi
import org.gdglille.devfest.models.ui.FiltersUi
import org.gdglille.devfest.models.ui.FormatUi

@FlowPreview
@ExperimentalCoroutinesApi
@ExperimentalSettingsApi
class ScheduleDao(
    private val db: Conferences4HallDatabase,
    private val settings: ObservableSettings
) {
    fun fetchSchedules(eventId: String): Flow<ImmutableMap<String, AgendaUi>> = combine(
        db.sessionQueries
            .selectSessions(eventId)
            .asFlow()
            .mapToList(Dispatchers.IO),
        db.sessionQueries
            .selectBreakSessions(eventId)
            .asFlow()
            .mapToList(Dispatchers.IO),
        db.categoryQueries
            .selectSelectedCategories(eventId)
            .asFlow()
            .mapToList(Dispatchers.IO),
        db.formatQueries
            .selectSelectedFormats(eventId)
            .asFlow()
            .mapToList(Dispatchers.IO),
        settings.getBooleanFlow("ONLY_FAVORITES", false),
        transform = { sessions, breaks, selectedCategories, selectedFormats, hasFavFilter ->
            val selectedCategoryIds = selectedCategories.map { it.id }
            val selectedFormatIds = selectedFormats.map { it.id }
            val hasCategoryFilter = selectedCategories.isNotEmpty()
            val hasFormatFilter = selectedFormats.isNotEmpty()
            val talkItems = sessions
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
                    val speakers = if (it.talk_id != null) {
                        db.sessionQueries
                            .selectSpeakersByTalkId(eventId, it.talk_id)
                            .executeAsList()
                    } else {
                        emptyList()
                    }
                    it.convertTalkItemUi(speakers = speakers)
                } + breaks.map { it.convertTalkItemUi() }
            sessions.distinctBy { it.date }
                .associate {
                    it.date to AgendaUi(
                        onlyFavorites = hasFavFilter,
                        talks = talkItems
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

    fun fetchFilters(eventId: String): Flow<FiltersUi> {
        return combine(
            db.categoryQueries
                .selectCategories(eventId)
                .asFlow()
                .mapToList(Dispatchers.IO),
            db.formatQueries
                .selectFormats(eventId)
                .asFlow()
                .mapToList(Dispatchers.IO),
            settings.getBooleanFlow("ONLY_FAVORITES", false),
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
    }

    fun fetchFiltersAppliedCount(eventId: String): Flow<Int> {
        return combine(
            db.categoryQueries
                .selectSelectedCategories(eventId)
                .asFlow()
                .mapToList(Dispatchers.IO),
            db.formatQueries
                .selectSelectedFormats(eventId)
                .asFlow()
                .mapToList(Dispatchers.IO),
            settings.getBooleanFlow("ONLY_FAVORITES", false),
            transform = { selectedCategories, selectedFormats, hasFavFilter ->
                return@combine selectedCategories.count() + selectedFormats.count() + if (hasFavFilter) 1 else 0
            }
        )
    }

    fun applyCategoryFilter(categoryUi: CategoryUi, eventId: String, selected: Boolean) =
        db.transaction {
            db.categoryQueries.upsertCategory(categoryUi.convertToEntity(eventId, selected))
        }

    fun applyFormatFilter(formatUi: FormatUi, eventId: String, selected: Boolean) =
        db.transaction {
            db.formatQueries.upsertFormat(formatUi.convertToEntity(eventId, selected))
        }

    fun applyFavoriteFilter(selected: Boolean) {
        settings.putBoolean("ONLY_FAVORITES", selected)
    }

    fun markAsFavorite(eventId: String, sessionId: String, isFavorite: Boolean) = db.transaction {
        db.sessionQueries.markAsFavorite(
            is_favorite = isFavorite,
            id = sessionId,
            event_id = eventId
        )
        if (isFavorite) return@transaction
        val onlyFavorites = settings.getBoolean("ONLY_FAVORITES", false)
        if (!onlyFavorites) return@transaction
        val countFavorites =
            db.sessionQueries.countSessionsByFavorite(eventId, true).executeAsOneOrNull()
        if (countFavorites != null && countFavorites != 0L) return@transaction
        settings.putBoolean("ONLY_FAVORITES", false)
    }

    fun saveAgenda(eventId: String, agendaV3: AgendaV3) = db.transaction {
        agendaV3.speakers.forEach { speaker ->
            db.speakerQueries.upsertSpeaker(speaker.convertToDb(eventId))
        }
        agendaV3.categories.forEach { category ->
            db.categoryQueries.upsertCategory(category.convertToDb(eventId))
        }
        agendaV3.formats.forEach { format ->
            db.formatQueries.upsertFormat(format.convertToDb(eventId))
        }
        agendaV3.talks.forEach { talk ->
            db.sessionQueries.upsertTalkSession(talk.convertToDb(eventId))
        }
        agendaV3.talks.forEach { talk ->
            talk.speakers.forEach {
                db.sessionQueries.upsertTalkWithSpeakersSession(talk.convertToDb(eventId, it))
            }
        }
        agendaV3.sessions.forEach { session ->
            db.sessionQueries.upsertSession(session.convertToDb(eventId))
        }
    }

    fun lastEtag(eventId: String): String? = settings.getStringOrNull("AGENDA_ETAG_$eventId")

    fun updateEtag(eventId: String, etag: String?) =
        etag?.let { settings.putString("AGENDA_ETAG_$eventId", it) }
}
