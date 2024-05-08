package org.gdglille.devfest.database

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import cafe.adriel.lyricist.Lyricist
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.getBooleanFlow
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
import org.gdglille.devfest.android.shared.resources.Strings
import org.gdglille.devfest.database.mappers.convertCategoryUi
import org.gdglille.devfest.database.mappers.convertFormatUi
import org.gdglille.devfest.database.mappers.convertTalkItemUi
import org.gdglille.devfest.database.mappers.convertToDb
import org.gdglille.devfest.database.mappers.convertToEntity
import org.gdglille.devfest.db.Conferences4HallDatabase
import org.gdglille.devfest.models.AgendaV4
import org.gdglille.devfest.models.Session
import org.gdglille.devfest.models.ui.AgendaUi
import org.gdglille.devfest.models.ui.CategoryUi
import org.gdglille.devfest.models.ui.FiltersUi
import org.gdglille.devfest.models.ui.FormatUi
import org.gdglille.devfest.models.ui.TalkItemUi
import kotlin.coroutines.CoroutineContext

@FlowPreview
@ExperimentalCoroutinesApi
@ExperimentalSettingsApi
class ScheduleDao(
    private val db: Conferences4HallDatabase,
    private val settings: ObservableSettings,
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
        settings.getBooleanFlow("ONLY_FAVORITES", false),
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
                    val speakers = if (it.session_talk_id != null) {
                        db.sessionQueries
                            .selectSpeakersByTalkId(eventId, it.session_talk_id)
                            .executeAsList()
                    } else {
                        emptyList()
                    }
                    it.convertTalkItemUi(speakers = speakers, strings = lyricist.strings)
                } + breaks.map { it.convertTalkItemUi(lyricist.strings) }
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

    fun fetchNextTalks(eventId: String, date: String): Flow<ImmutableList<TalkItemUi>> {
        val dateTime = date.toLocalDateTime()
        return db.sessionQueries
            .selectSessions(eventId)
            .asFlow()
            .mapToList(dispatcher)
            .map { sessions ->
                val nextAgenda = sessions
                    .filter { dateTime < it.start_time.toLocalDateTime() }
                    .map {
                        val speakers = if (it.session_talk_id != null) {
                            db.sessionQueries
                                .selectSpeakersByTalkId(eventId, it.session_talk_id)
                                .executeAsList()
                        } else {
                            emptyList()
                        }
                        it.convertTalkItemUi(speakers = speakers, strings = lyricist.strings)
                    }
                    .groupBy { it.startTime }
                val toImmutableList = nextAgenda.values.first().toImmutableList()
                return@map toImmutableList
            }
    }

    fun fetchFilters(eventId: String): Flow<FiltersUi> {
        return combine(
            db.categoryQueries
                .selectCategories(eventId)
                .asFlow()
                .mapToList(dispatcher),
            db.formatQueries
                .selectFormats(eventId)
                .asFlow()
                .mapToList(dispatcher),
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
                .mapToList(dispatcher),
            db.formatQueries
                .selectSelectedFormats(eventId)
                .asFlow()
                .mapToList(dispatcher),
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

    fun saveAgenda(eventId: String, agenda: AgendaV4) = db.transaction {
        agenda.speakers.forEach { speaker ->
            db.speakerQueries.upsertSpeaker(speaker.convertToDb(eventId))
        }
        agenda.categories.forEach { category ->
            db.categoryQueries.upsertCategory(category.convertToDb(eventId))
        }
        agenda.formats.forEach { format ->
            db.formatQueries.upsertFormat(format.convertToDb(eventId))
        }
        agenda.sessions.forEach { session ->
            when (session) {
                is Session.Talk -> {
                    db.sessionQueries.upsertTalkSession(session.convertToDb(eventId))
                }
                is Session.Event -> {
                    db.sessionQueries.upsertEventSession(session.convertToDb(eventId))
                }
            }
        }
        agenda.sessions.filterIsInstance<Session.Talk>().forEach { session ->
            session.speakers.forEach {
                db.sessionQueries.upsertTalkWithSpeakersSession(session.convertToDb(eventId, it))
            }
        }
        agenda.schedules.forEach { schedule ->
            val clazz = if (agenda.sessions.find { it.id == schedule.sessionId } is Session.Talk) {
                Session.Talk::class
            } else {
                Session.Event::class
            }
            db.sessionQueries.upsertSession(schedule.convertToDb(eventId, clazz))
        }
        clean(eventId, agenda)
    }

    private fun clean(eventId: String, agenda: AgendaV4) = db.transaction {
        val diffSpeakers = db.speakerQueries
            .diffSpeakers(event_id = eventId, id = agenda.speakers.map { it.id })
            .executeAsList()
        db.speakerQueries.deleteSpeakers(event_id = eventId, id = diffSpeakers)
        val diffCategories = db.categoryQueries
            .diffCategories(event_id = eventId, id = agenda.categories.map { it.id })
            .executeAsList()
        db.categoryQueries.deleteCategories(event_id = eventId, id = diffCategories)
        val diffFormats = db.formatQueries
            .diffFormats(event_id = eventId, id = agenda.formats.map { it.id })
            .executeAsList()
        db.formatQueries.deleteFormats(event_id = eventId, id = diffFormats)
        val talkIds = agenda.sessions.map { it.id }
        val diffTalkSession = db.sessionQueries
            .diffTalkSessions(event_id = eventId, id = talkIds)
            .executeAsList()
        db.sessionQueries.deleteTalkSessions(event_id = eventId, id = diffTalkSession)
        val diffTalkWithSpeakers = db.sessionQueries
            .diffTalkWithSpeakers(event_id = eventId, talk_id = talkIds)
            .executeAsList()
        db.sessionQueries.deleteTalkWithSpeakers(event_id = eventId, talk_id = diffTalkWithSpeakers)
        val diffSessions = db.sessionQueries
            .diffSessions(event_id = eventId, id = talkIds)
            .executeAsList()
        db.sessionQueries.deleteSessions(event_id = eventId, id = diffSessions)
    }

    fun lastEtag(eventId: String): String? = settings.getStringOrNull("AGENDA_ETAG_$eventId")

    fun updateEtag(eventId: String, etag: String?) =
        etag?.let { settings.putString("AGENDA_ETAG_$eventId", it) }
}
