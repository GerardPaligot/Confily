package com.paligot.confily.core.database

import com.paligot.confily.core.database.mappers.convertToDb
import com.paligot.confily.db.ConfilyDatabase
import com.paligot.confily.models.AgendaV4
import com.paligot.confily.models.Session
import com.russhwolf.settings.ObservableSettings

class AgendaDao(
    private val db: ConfilyDatabase,
    private val settings: ObservableSettings
) {
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
