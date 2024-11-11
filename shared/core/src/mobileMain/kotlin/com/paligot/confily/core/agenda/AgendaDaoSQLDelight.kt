package com.paligot.confily.core.agenda

import com.paligot.confily.db.ConfilyDatabase
import com.paligot.confily.models.AgendaV4
import com.paligot.confily.models.EventV3
import com.paligot.confily.models.PartnersActivities
import com.paligot.confily.models.QuestionAndResponse
import com.paligot.confily.models.Session
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format

class AgendaDaoSQLDelight(
    private val db: ConfilyDatabase,
    private val hasSvgSupport: Boolean
) : AgendaDao {
    override fun saveAgenda(eventId: String, agenda: AgendaV4) = db.transaction {
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

    override fun insertEvent(event: EventV3, qAndA: List<QuestionAndResponse>) = db.transaction {
        val eventDb = event.convertToModelDb()
        db.eventQueries.insertEvent(
            id = eventDb.id,
            name = eventDb.name,
            formatted_address = eventDb.formatted_address,
            address = eventDb.address,
            latitude = eventDb.latitude,
            longitude = eventDb.longitude,
            date = eventDb.date,
            start_date = eventDb.start_date,
            end_date = eventDb.end_date,
            coc = eventDb.coc,
            openfeedback_project_id = eventDb.openfeedback_project_id,
            contact_email = eventDb.contact_email,
            contact_phone = eventDb.contact_phone,
            twitter = eventDb.twitter,
            twitter_url = eventDb.twitter_url,
            linkedin = eventDb.linkedin,
            linkedin_url = eventDb.linkedin_url,
            faq_url = eventDb.faq_url,
            coc_url = eventDb.coc_url,
            updated_at = eventDb.updated_at
        )
        qAndA.forEach { qAndA ->
            db.qAndAQueries.insertQAndA(
                qAndA.order.toLong(),
                eventDb.id,
                qAndA.question,
                qAndA.response
            )
            qAndA.actions.forEach {
                db.qAndAQueries.insertQAndAAction(
                    id = "${qAndA.id}-${it.order}",
                    order_ = it.order.toLong(),
                    event_id = eventDb.id,
                    qanda_id = qAndA.order.toLong(),
                    label = it.label,
                    url = it.url
                )
            }
        }
        event.menus.forEach {
            db.menuQueries.insertMenu(it.name, it.dish, it.accompaniment, it.dessert, event.id)
        }
        db.featuresActivatedQueries.insertFeatures(
            event_id = eventDb.id,
            has_networking = event.features.hasNetworking,
            has_speaker_list = event.features.hasSpeakerList,
            has_partner_list = event.features.hasPartnerList,
            has_menus = event.features.hasMenus,
            has_qanda = event.features.hasQAndA,
            has_billet_web_ticket = event.features.hasBilletWebTicket
        )
    }

    override fun insertPartners(eventId: String, partners: PartnersActivities) = db.transaction {
        partners.types.forEachIndexed { index, type ->
            db.partnerQueries.insertPartnerType(
                order_ = index.toLong(),
                name = type,
                event_id = eventId
            )
        }
        partners.partners.forEach { partner ->
            db.partnerQueries.insertPartner(
                id = partner.id,
                name = partner.name,
                description = partner.description,
                event_id = eventId,
                logo_url = if (hasSvgSupport) {
                    partner.media.svg
                } else if (partner.media.pngs != null) {
                    partner.media.pngs!!._250
                } else {
                    partner.media.svg
                },
                formatted_address = partner.address?.formatted,
                address = partner.address?.address,
                latitude = partner.address?.lat,
                longitude = partner.address?.lng
            )
            partner.socials.forEach { social ->
                db.partnerQueries.insertPartnerSocial(
                    url = social.url,
                    type = social.type.name,
                    partner_id = partner.id,
                    event_id = eventId
                )
            }
            partner.types.forEach { type ->
                db.partnerQueries.insertPartnerAndType(
                    id = "${partner.id}-$type",
                    partner_id = partner.id,
                    sponsor_id = type,
                    event_id = eventId
                )
            }
            partner.jobs.forEach { job ->
                db.partnerQueries.insertJob(
                    url = job.url,
                    partner_id = partner.id,
                    event_id = eventId,
                    title = job.title,
                    company_name = job.companyName,
                    location = job.location,
                    salary_max = job.salary?.max?.toLong(),
                    salary_min = job.salary?.min?.toLong(),
                    salary_recurrence = job.salary?.recurrence,
                    requirements = job.requirements,
                    publish_date = job.publishDate,
                    propulsed = job.propulsed
                )
            }
        }
        partners.activities.forEach {
            db.partnerQueries.insertPartnerActivity(
                id = it.id,
                name = it.name,
                date = LocalDateTime.parse(it.startTime).date.format(LocalDate.Formats.ISO),
                start_time = it.startTime,
                end_time = it.endTime,
                partner_id = it.partnerId,
                event_id = eventId
            )
        }
    }
}
