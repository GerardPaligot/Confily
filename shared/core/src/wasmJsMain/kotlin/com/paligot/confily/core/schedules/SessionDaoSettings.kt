package com.paligot.confily.core.schedules

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
import com.paligot.confily.core.socials.SocialQueries
import com.paligot.confily.core.speakers.SpeakerQueries
import com.paligot.confily.models.AgendaV4
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDateTime

class SessionDaoSettings(
    private val settings: ConferenceSettings,
    private val sessionQueries: SessionQueries,
    private val categoryQueries: CategoryQueries,
    private val formatQueries: FormatQueries,
    private val speakerQueries: SpeakerQueries,
    private val socialQueries: SocialQueries
) : SessionDao {
    override fun fetchSession(eventId: String, sessionId: String): Flow<Session> = combine(
        flow = flowOf(sessionQueries.getSpeakersByTalkId(eventId, sessionId)),
        flow2 = sessionQueries.selectSessionByTalkId(eventId, sessionId),
        transform = { speakers, session ->
            session.mapToSessionEntity(speakers = speakers.map { it.mapToEntity() })
        }
    )

    override fun fetchEventSession(eventId: String, sessionId: String): Flow<EventSession> =
        sessionQueries.selectEventSessionById(eventId, sessionId)
            .map { it.mapToEntity() }

    override fun fetchSessionsFiltered(eventId: String): Flow<List<SessionItem>> = combine(
        flow = sessionQueries.selectSessions(eventId),
        flow2 = categoryQueries.selectCategories(eventId),
        flow3 = formatQueries.selectFormats(eventId),
        flow4 = settings.fetchOnlyFavoritesFlag(),
        transform = { sessions, selectedCategories, selectedFormats, hasFavFilter ->
            sessions
                .sessionFiltered(selectedCategories, selectedFormats, hasFavFilter)
                .map { session ->
                    val speakers = if (session.session.sessionTalkId != null) {
                        sessionQueries
                            .getSpeakersByTalkId(eventId, session.session.sessionTalkId)
                            .map { it.mapToEntity() }
                    } else {
                        emptyList()
                    }
                    session.mapToEntity(speakers)
                }
        }
    )

    private fun List<SelectSessionsDb>.sessionFiltered(
        selectedCategories: List<CategoryDb>,
        selectedFormats: List<FormatDb>,
        hasFavFilter: Boolean
    ): List<SelectSessionsDb> {
        val selectedCategoryIds = selectedCategories.map { it.id }
        val selectedFormatIds = selectedFormats.map { it.id }
        val hasCategoryFilter = selectedCategories.isNotEmpty()
        val hasFormatFilter = selectedFormats.isNotEmpty()
        return this.filter {
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
    }

    override fun fetchNextSessions(
        eventId: String,
        date: String
    ): Flow<List<SessionItem>> {
        val dateTime = LocalDateTime.parse(date)
        return sessionQueries
            .selectSessions(eventId)
            .map { sessions ->
                sessions
                    .filter { dateTime <= LocalDateTime.parse(it.session.date) }
                    .map {
                        val speakers = it.session.sessionTalkId?.let { talkId ->
                            sessionQueries.getSpeakersByTalkId(eventId, talkId)
                                .map { it.mapToEntity() }
                        } ?: run { emptyList() }
                        it.mapToEntity(speakers = speakers)
                    }
            }
    }

    override fun fetchSessionsBySpeakerId(
        eventId: String,
        speakerId: String
    ): Flow<List<SessionItem>> {
        return sessionQueries
            .selectTalksBySpeakerId(eventId, speakerId)
            .map { talks ->
                talks
                    .map { talk ->
                        talk.mapToEntity(
                            session = sessionQueries
                                .getSessionByTalkId(eventId, talk.session.id),
                            speakers = sessionQueries
                                .getSpeakersByTalkId(eventId, talk.session.id)
                        )
                    }
            }
    }

    override fun fetchEventSessions(eventId: String): Flow<List<EventSessionItem>> = sessionQueries
        .selectBreakSessions(eventId)
        .map { eventSessions -> eventSessions.map { it.mapToItemEntity() } }

    override fun fetchCategories(eventId: String): Flow<List<SelectableCategory>> = categoryQueries
        .selectCategories(eventId)
        .map { categories -> categories.map { it.mapTopEntity() } }

    override fun fetchSelectedCategories(eventId: String): Flow<List<Category>> = categoryQueries
        .selectSelectedCategories(eventId, selected = true)
        .map { categories -> categories.map { it.mapToEntity() } }

    override fun fetchCountSelectedCategories(eventId: String): Flow<Long> = categoryQueries
        .selectSelectedCategories(eventId)
        .map { it.size.toLong() }

    override fun fetchFormats(eventId: String): Flow<List<SelectableFormat>> = formatQueries
        .selectFormats(eventId)
        .map { formats -> formats.map { it.mapTopEntity() } }

    override fun fetchSelectedFormats(eventId: String): Flow<List<Format>> = formatQueries
        .selectSelectedFormats(eventId, selected = true)
        .map { formats -> formats.map { it.mapToEntity() } }

    override fun fetchCountSelectedFormats(eventId: String): Flow<Long> = formatQueries
        .selectSelectedFormats(eventId)
        .map { it.size.toLong() }

    override fun applyFavoriteFilter(selected: Boolean) {
        settings.upsertOnlyFavoritesFlag(selected)
    }

    override fun applyCategoryFilter(eventId: String, categoryId: String, selected: Boolean) {
        categoryQueries.updateSelectedCategory(selected, categoryId)
    }

    override fun applyFormatFilter(eventId: String, formatId: String, selected: Boolean) {
        formatQueries.updateSelectedFormat(selected, formatId)
    }

    override fun markAsFavorite(eventId: String, sessionId: String, isFavorite: Boolean) {
        sessionQueries.markAsFavorite(eventId, sessionId, isFavorite)
        if (isFavorite) return
        val onlyFavorites = settings.getOnlyFavoritesFlag()
        if (!onlyFavorites) return
        val countFavorites = sessionQueries.countSessionsByFavorite(eventId, true)
        if (countFavorites != 0) return
        settings.upsertOnlyFavoritesFlag(false)
    }

    override fun insertAgenda(eventId: String, agenda: AgendaV4) {
        agenda.speakers.forEach { speaker ->
            speakerQueries.upsertSpeaker(speaker.convertToDb(eventId))
            speaker.socials.forEach { social ->
                socialQueries.upsertSocial(social.convertToDb(eventId, speaker.id))
            }
        }
        agenda.categories.forEach { category ->
            categoryQueries.upsertCategory(category.convertToDb(eventId))
        }
        agenda.formats.forEach { format ->
            formatQueries.upsertFormat(format.convertToDb(eventId))
        }
        agenda.sessions.forEachIndexed { indexSession, session ->
            when (session) {
                is com.paligot.confily.models.Session.Talk -> {
                    sessionQueries.upsertTalkSession(session.convertToDb(eventId))
                    session.speakers.forEachIndexed { index, speaker ->
                        sessionQueries.upsertTalkWithSpeakers(
                            session.convertToDb(eventId, "$indexSession:$index", speaker)
                        )
                    }
                }

                is com.paligot.confily.models.Session.Event -> {
                    sessionQueries.upsertEventSession(session.convertToDb(eventId))
                }
            }
        }
        agenda.schedules.forEach { schedule ->
            val clazz = if (agenda.sessions.find { it.id == schedule.sessionId } is com.paligot.confily.models.Session.Talk) {
                com.paligot.confily.models.Session.Talk::class
            } else {
                com.paligot.confily.models.Session.Event::class
            }
            sessionQueries.upsertSession(schedule.convertToDb(eventId, clazz))
        }
        clean(eventId, agenda)
    }

    private fun clean(eventId: String, agenda: AgendaV4) {
        val diffSpeakers = speakerQueries
            .diffSpeakers(eventId = eventId, ids = agenda.speakers.map { it.id })
        speakerQueries.deleteSpeakers(ids = diffSpeakers)
        val diffCategories = categoryQueries
            .diffCategories(eventId = eventId, ids = agenda.categories.map { it.id })
        categoryQueries.deleteCategories(ids = diffCategories)
        val diffFormats = formatQueries
            .diffFormats(eventId = eventId, ids = agenda.formats.map { it.id })
        formatQueries.deleteFormats(ids = diffFormats)
        val talkIds = agenda.sessions.map { it.id }
        val diffTalkSession = sessionQueries
            .diffTalkSessions(eventId = eventId, ids = talkIds)
        sessionQueries.deleteTalkSessions(ids = diffTalkSession)
        val diffTalkWithSpeakers = sessionQueries
            .diffTalkWithSpeakers(eventId = eventId, ids = talkIds)
        sessionQueries.deleteTalkWithSpeakers(ids = diffTalkWithSpeakers)
        val diffSessions = sessionQueries
            .diffSessions(eventId = eventId, ids = talkIds)
        sessionQueries.deleteSessions(ids = diffSessions)
    }
}
