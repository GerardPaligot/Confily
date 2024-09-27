package com.paligot.confily.core.database

import com.paligot.confily.core.Platform
import com.paligot.confily.core.database.mappers.convertToDb
import com.paligot.confily.db.ConfilyDatabase
import com.paligot.confily.models.AgendaV4
import com.paligot.confily.models.PartnerV2
import com.paligot.confily.models.Session
import com.russhwolf.settings.ObservableSettings

class AgendaDao(
    private val db: ConfilyDatabase,
    private val settings: ObservableSettings,
    private val platform: Platform
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

    fun insertPartners(eventId: String, partners: Map<String, List<PartnerV2>>) = db.transaction {
        partners.keys.forEachIndexed { index, type ->
            db.partnerQueries.insertPartnerType(
                order_ = index.toLong(),
                name = type,
                event_id = eventId
            )
        }
        partners.entries.forEach { entry ->
            entry.value.forEach { partner ->
                db.partnerQueries.insertPartner(
                    id = partner.id,
                    name = partner.name,
                    description = partner.description,
                    event_id = eventId,
                    type_id = entry.key,
                    type = entry.key,
                    logo_url = if (platform.hasSupportSVG) {
                        partner.media.svg
                    } else if (partner.media.pngs != null) {
                        partner.media.pngs!!._250
                    } else {
                        partner.media.svg
                    },
                    site_url = partner.siteUrl,
                    twitter_url = partner.twitterUrl,
                    twitter_message = partner.twitterMessage,
                    linkedin_url = partner.linkedinUrl,
                    linkedin_message = partner.linkedinMessage,
                    formatted_address = partner.address?.formatted,
                    address = partner.address?.address,
                    latitude = partner.address?.lat,
                    longitude = partner.address?.lng
                )
                db.partnerQueries.insertPartnerAndType(
                    id = "${partner.id}-${entry.key}",
                    partner_id = partner.id,
                    sponsor_id = entry.key,
                    event_id = eventId
                )
                partner.jobs.forEach {
                    db.partnerQueries.insertJob(
                        url = it.url,
                        partner_id = partner.id,
                        event_id = eventId,
                        title = it.title,
                        company_name = it.companyName,
                        location = it.location,
                        salary_max = it.salary?.max?.toLong(),
                        salary_min = it.salary?.min?.toLong(),
                        salary_recurrence = it.salary?.recurrence,
                        requirements = it.requirements,
                        publish_date = it.publishDate,
                        propulsed = it.propulsed
                    )
                }
            }
        }
    }
}
