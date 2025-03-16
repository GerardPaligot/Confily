package com.paligot.confily.core.schedules

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import com.paligot.confily.core.agenda.convertToDb
import com.paligot.confily.core.kvalue.ConferenceSettings
import com.paligot.confily.core.schedules.entities.Category
import com.paligot.confily.core.schedules.entities.EventSession
import com.paligot.confily.core.schedules.entities.EventSessionItem
import com.paligot.confily.core.schedules.entities.Format
import com.paligot.confily.core.schedules.entities.SelectableCategory
import com.paligot.confily.core.schedules.entities.SelectableFormat
import com.paligot.confily.core.schedules.entities.Session
import com.paligot.confily.core.schedules.entities.SessionItem
import com.paligot.confily.db.ConfilyDatabase
import com.paligot.confily.db.SelectSelectedCategories
import com.paligot.confily.db.SelectSelectedFormats
import com.paligot.confily.db.SelectSessions
import com.paligot.confily.models.AgendaV4
import com.russhwolf.settings.ExperimentalSettingsApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDateTime
import kotlin.coroutines.CoroutineContext

@FlowPreview
@ExperimentalCoroutinesApi
@ExperimentalSettingsApi
class SessionDaoSQLDelight(
    private val db: ConfilyDatabase,
    private val settings: ConferenceSettings,
    private val dispatcher: CoroutineContext
) : SessionDao {
    override fun fetchSession(eventId: String, sessionId: String): Flow<Session> =
        db.transactionWithResult {
            combine(
                flow = db.sessionQueries
                    .selectSpeakersByTalkId(event_id = eventId, talk_id = sessionId)
                    .asFlow()
                    .mapToList(dispatcher),
                flow2 = db.eventQueries
                    .selectOpenfeedbackProjectId(eventId)
                    .asFlow()
                    .mapToOne(dispatcher),
                flow3 = db.sessionQueries
                    .selectSessionByTalkId(eventId)
                    .asFlow()
                    .mapToOne(dispatcher),
                transform = { speakers, openfeedback, talk ->
                    talk.mapToEntity(speakers.map { it.mapToEntity() }, openfeedback)
                }
            )
        }

    override fun fetchEventSession(eventId: String, sessionId: String): Flow<EventSession> =
        db.eventSessionQueries
            .selectEventSessionById(eventId, eventSessionMapper)
            .asFlow()
            .mapToOne(dispatcher)

    override fun fetchSessionsFiltered(eventId: String): Flow<List<SessionItem>> = combine(
        flow = db.sessionQueries
            .selectSessions(eventId)
            .asFlow()
            .mapToList(dispatcher),
        flow2 = db.categoryQueries
            .selectSelectedCategories(eventId)
            .asFlow()
            .mapToList(dispatcher),
        flow3 = db.formatQueries
            .selectSelectedFormats(eventId)
            .asFlow()
            .mapToList(dispatcher),
        flow4 = settings.fetchOnlyFavoritesFlag(),
        transform = { sessions, categories, formats, hasFavFilter ->
            val speakers = db.sessionQueries
                .selectSpeakersByTalkIds(eventId, sessions.map { it.id })
                .executeAsList()
            sessions
                .sessionFiltered(categories, formats, hasFavFilter)
                .map { session ->
                    val speakersByTalk = speakers
                        .filter { it.talk_id == session.id }
                        .map { it.mapToEntity() }
                    session.mapToEntity(speakersByTalk)
                }
        }
    )

    private fun List<SelectSessions>.sessionFiltered(
        selectedCategories: List<SelectSelectedCategories>,
        selectedFormats: List<SelectSelectedFormats>,
        hasFavFilter: Boolean
    ): List<SelectSessions> {
        val selectedCategoryIds = selectedCategories.map { it.id }
        val selectedFormatIds = selectedFormats.map { it.id }
        val hasCategoryFilter = selectedCategories.isNotEmpty()
        val hasFormatFilter = selectedFormats.isNotEmpty()
        return this.filter {
            val selectedCategory =
                if (hasCategoryFilter) selectedCategoryIds.contains(it.category_id) else true
            val selectedFormat =
                if (hasFormatFilter) selectedFormatIds.contains(it.format_id) else true
            val selectedFav = if (hasFavFilter) it.is_favorite else true
            val should = when {
                hasCategoryFilter && hasFormatFilter.not() && hasFavFilter.not() -> selectedCategory
                hasCategoryFilter && hasFormatFilter.not() && hasFavFilter -> selectedCategory && selectedFav
                hasCategoryFilter.not() && hasFormatFilter && hasFavFilter.not() -> selectedFormat
                hasCategoryFilter.not() && hasFormatFilter && hasFavFilter -> selectedFormat && selectedFav
                hasCategoryFilter && hasFormatFilter && hasFavFilter.not() -> selectedCategory && selectedFormat
                hasCategoryFilter.not() && hasFormatFilter.not() && hasFavFilter -> selectedFav
                hasCategoryFilter && hasFormatFilter && hasFavFilter -> selectedCategory && selectedFormat && selectedFav
                else -> true
            }
            should
        }
    }

    override fun fetchNextSessions(
        eventId: String,
        date: String
    ): Flow<List<SessionItem>> = db.transactionWithResult {
        val dateTime = LocalDateTime.parse(date)
        return@transactionWithResult db.sessionQueries
            .selectSessions(eventId)
            .asFlow()
            .mapToList(dispatcher)
            .flatMapMerge { sessions ->
                val nextSessions = sessions
                    .filter { dateTime < LocalDateTime.parse(it.start_time) }
                val sessionIds = nextSessions.mapNotNull { it.id }
                db.sessionQueries
                    .selectSpeakersByTalkIds(eventId, sessionIds)
                    .asFlow()
                    .mapToList(dispatcher)
                    .map { allSpeakers ->
                        nextSessions.map { session ->
                            val speakers = allSpeakers
                                .filter { it.talk_id == session.id }
                                .map { it.mapToEntity() }
                            session.mapToEntity(speakers)
                        }
                    }
            }
    }

    override fun fetchSessionsBySpeakerId(
        eventId: String,
        speakerId: String
    ): Flow<List<SessionItem>> = db.sessionQueries
        .selectTalksBySpeakerId(eventId, speakerId)
        .asFlow()
        .mapToList(dispatcher)
        .map { sessions ->
            sessions.map { session ->
                session.mapToEntity(
                    db.sessionQueries
                        .selectSpeakersByTalkId(eventId, session.id)
                        .executeAsList()
                        .map { it.mapToEntity() }
                )
            }
        }

    override fun fetchEventSessions(eventId: String): Flow<List<EventSessionItem>> =
        db.eventSessionQueries
            .selectEventSessions(eventId, eventSessionItemMapper)
            .asFlow()
            .mapToList(dispatcher)

    override fun fetchCategories(eventId: String): Flow<List<SelectableCategory>> =
        db.categoryQueries
            .selectCategories(eventId, categoryMapper)
            .asFlow()
            .mapToList(dispatcher)

    override fun fetchSelectedCategories(eventId: String): Flow<List<Category>> =
        db.categoryQueries
            .selectSelectedCategories(eventId, categoryMapper)
            .asFlow()
            .mapToList(dispatcher)

    override fun fetchCountSelectedCategories(eventId: String): Flow<Long> = db.categoryQueries
        .countSelectedCategories(eventId)
        .asFlow()
        .mapToOne(dispatcher)

    override fun fetchFormats(eventId: String): Flow<List<SelectableFormat>> = db.formatQueries
        .selectFormats(eventId, formatMapper)
        .asFlow()
        .mapToList(dispatcher)

    override fun fetchSelectedFormats(eventId: String): Flow<List<Format>> = db.formatQueries
        .selectSelectedFormats(eventId, formatMapper)
        .asFlow()
        .mapToList(dispatcher)

    override fun fetchCountSelectedFormats(eventId: String): Flow<Long> = db.formatQueries
        .countSelectedFormats(eventId)
        .asFlow()
        .mapToOne(dispatcher)

    override fun applyFavoriteFilter(selected: Boolean) {
        settings.upsertOnlyFavoritesFlag(selected)
    }

    override fun applyCategoryFilter(eventId: String, categoryId: String, selected: Boolean) =
        db.transaction {
            db.categoryQueries.updateSelectedCategory(selected, categoryId, eventId)
        }

    override fun applyFormatFilter(eventId: String, formatId: String, selected: Boolean) =
        db.transaction {
            db.formatQueries.updateSelectedFormat(selected, formatId, eventId)
        }

    override fun markAsFavorite(eventId: String, sessionId: String, isFavorite: Boolean) =
        db.transaction {
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

    override fun insertAgenda(eventId: String, agenda: AgendaV4) = db.transaction {
        agenda.speakers.forEach { speaker ->
            db.speakerQueries.upsertSpeaker(speaker.convertToDb(eventId))
            speaker.socials.forEach {
                db.socialQueries.insertSocial(it.url, it.type.name.lowercase(), speaker.id, eventId)
            }
        }
        agenda.categories.forEach { category ->
            db.categoryQueries.upsertCategory(category.convertToDb(eventId))
        }
        agenda.formats.forEach { format ->
            db.formatQueries.upsertFormat(format.convertToDb(eventId))
        }
        agenda.sessions.forEach { session ->
            when (session) {
                is com.paligot.confily.models.Session.Talk -> {
                    db.sessionQueries.upsertTalkSession(session.convertToDb(eventId))
                }

                is com.paligot.confily.models.Session.Event -> {
                    db.eventSessionQueries.upsertEventSession(session.convertToDb(eventId))
                }
            }
        }
        agenda.sessions.filterIsInstance<com.paligot.confily.models.Session.Talk>()
            .forEach { session ->
                session.speakers.forEach {
                    db.sessionQueries.upsertTalkWithSpeakersSession(
                        session.convertToDb(
                            eventId,
                            it
                        )
                    )
                }
            }
        agenda.schedules.forEach { schedule ->
            val clazz =
                if (agenda.sessions.find { it.id == schedule.sessionId } is com.paligot.confily.models.Session.Talk) {
                    com.paligot.confily.models.Session.Talk::class
                } else {
                    com.paligot.confily.models.Session.Event::class
                }
            db.scheduleQueries.upsertSchedule(schedule.convertToDb(eventId, clazz))
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
}
