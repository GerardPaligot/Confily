package org.gdglille.devfest.database

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.getBooleanFlow
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapMerge
import org.gdglille.devfest.Platform
import org.gdglille.devfest.database.mappers.convertTalkItemUi
import org.gdglille.devfest.database.mappers.convertToDb
import org.gdglille.devfest.db.Conferences4HallDatabase
import org.gdglille.devfest.models.AgendaUi
import org.gdglille.devfest.models.AgendaV3

@FlowPreview
@ExperimentalCoroutinesApi
@ExperimentalSettingsApi
class ScheduleDao(
    private val db: Conferences4HallDatabase,
    private val settings: ObservableSettings,
    private val platform: Platform
) {
    fun fetchSchedules(eventId: String, date: String): Flow<AgendaUi> {
        return settings.getBooleanFlow("ONLY_FAVORITES", false)
            .flatMapMerge {
                combine(
                    db.sessionQueries
                        .selectSessions(eventId, date)
                        .asFlow()
                        .mapToList(Dispatchers.IO),
                    db.sessionQueries
                        .selectBreakSessions(eventId, date)
                        .asFlow()
                        .mapToList(Dispatchers.IO),
                    transform = { sessions, breaks ->
                        val onlyFavorites = settings.getBoolean("ONLY_FAVORITES", false)
                        val talks =
                            if (onlyFavorites) sessions.filter { it.is_favorite == onlyFavorites } else sessions
                        val talkItems = talks
                            .map {
                                val speakers = if (it.talk_id != null) {
                                    db.sessionQueries
                                        .selectSpeakersByTalkId(eventId, it.talk_id)
                                        .executeAsList()
                                } else {
                                    emptyList()
                                }
                                it.convertTalkItemUi(speakers)
                            } + breaks.map { it.convertTalkItemUi(platform::getString) }
                        AgendaUi(
                            onlyFavorites = onlyFavorites,
                            talks = talkItems
                                .sortedBy { it.slotTime }
                                .groupBy { it.slotTime }
                                .mapValues { entry ->
                                    entry.value.sortedBy { it.order }.toImmutableList()
                                }
                                .toImmutableMap()
                        )
                    }
                )
            }
    }

    fun isFavoriteToggled(): Flow<Boolean> = settings.getBooleanFlow("ONLY_FAVORITES")

    fun toggleFavoriteFiltering() {
        val isFavorite = settings.getBoolean("ONLY_FAVORITES", false)
        settings.putBoolean("ONLY_FAVORITES", !isFavorite)
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
