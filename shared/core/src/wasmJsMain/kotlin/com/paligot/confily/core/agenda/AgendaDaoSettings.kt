package com.paligot.confily.core.agenda

import com.paligot.confily.core.events.EventQueries
import com.paligot.confily.core.events.FeaturesActivatedQueries
import com.paligot.confily.core.events.MenuQueries
import com.paligot.confily.core.events.QAndAQueries
import com.paligot.confily.core.partners.PartnerQueries
import com.paligot.confily.core.schedules.CategoryQueries
import com.paligot.confily.core.schedules.FormatQueries
import com.paligot.confily.core.schedules.SessionQueries
import com.paligot.confily.core.speakers.SpeakerQueries
import com.paligot.confily.models.AgendaV4
import com.paligot.confily.models.EventV3
import com.paligot.confily.models.PartnersActivities
import com.paligot.confily.models.QuestionAndResponse
import com.paligot.confily.models.Session

class AgendaDaoSettings(
    private val eventQueries: EventQueries,
    private val featuresQueries: FeaturesActivatedQueries,
    private val menusQueries: MenuQueries,
    private val qAndAQueries: QAndAQueries,
    private val sessionQueries: SessionQueries,
    private val speakerQueries: SpeakerQueries,
    private val categoryQueries: CategoryQueries,
    private val formatQueries: FormatQueries,
    private val partnerQueries: PartnerQueries,
    private val hasSvgSupport: Boolean
) : AgendaDao {
    override fun saveAgenda(eventId: String, agenda: AgendaV4) {
        agenda.speakers.forEach { speaker ->
            speakerQueries.upsertSpeaker(speaker.convertToDb(eventId))
        }
        agenda.categories.forEach { category ->
            categoryQueries.upsertCategory(category.convertToDb(eventId))
        }
        agenda.formats.forEach { format ->
            formatQueries.upsertFormat(format.convertToDb(eventId))
        }
        agenda.sessions.forEachIndexed { indexSession, session ->
            when (session) {
                is Session.Talk -> {
                    sessionQueries.upsertTalkSession(session.convertToDb(eventId))
                    session.speakers.forEachIndexed { index, speaker ->
                        sessionQueries.upsertTalkWithSpeakers(
                            session.convertToDb(eventId, "$indexSession:$index", speaker)
                        )
                    }
                }

                is Session.Event -> {
                    sessionQueries.upsertEventSession(session.convertToDb(eventId))
                }
            }
        }
        agenda.schedules.forEach { schedule ->
            val clazz = if (agenda.sessions.find { it.id == schedule.sessionId } is Session.Talk) {
                Session.Talk::class
            } else {
                Session.Event::class
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

    override fun insertEvent(event: EventV3, qAndA: List<QuestionAndResponse>) {
        val eventDb = event.convertToModelDb()
        eventQueries.insertEvent(eventDb)
        qAndA.forEach { qAndA ->
            qAndAQueries.insertQAndA(qAndA.convertToModelDb(eventDb.id))
            qAndA.actions.forEach {
                qAndAQueries.insertQAndAAction(it.convertToModelDb(eventDb.id, qAndA.id))
            }
        }
        event.menus.forEach {
            menusQueries.insertMenu(it.convertToModelDb(eventDb.id))
        }
        featuresQueries.insertFeatures(event.features.convertToModelDb(eventDb.id))
    }

    override fun insertPartners(eventId: String, partners: PartnersActivities) {
        partners.partners.forEach { partner ->
            partner.types.forEach { type ->
                partnerQueries.insertPartner(partner.convertToDb(eventId, type, hasSvgSupport))
            }
            partner.socials.forEach { social ->
                partnerQueries.insertPartnerSocial(social.convertToDb(eventId, partner.id))
            }
            partner.jobs.forEach { job ->
                partnerQueries.insertJob(job.convertToDb(eventId, partner.id))
            }
        }
    }
}
