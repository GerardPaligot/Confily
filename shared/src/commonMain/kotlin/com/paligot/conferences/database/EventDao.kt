package com.paligot.conferences.database

import com.paligot.conferences.db.Conferences4HallDatabase
import com.paligot.conferences.models.Event
import com.paligot.conferences.repositories.EventInfoUi
import com.paligot.conferences.repositories.EventUi
import com.paligot.conferences.repositories.PartnerGroupsUi
import com.paligot.conferences.repositories.PartnerItemUi

class EventDao(private val db: Conferences4HallDatabase, private val eventId: String) {
    private val eventMapper = { _: String, name: String, address: String, date: String, twitter: String?,
                                twitter_url: String?, linkedin: String?, linkedin_url: String?, faq_url: String,
                                coc_url: String ->
        EventInfoUi(
            name = name,
            address = address,
            date = date,
            twitter = twitter,
            twitterUrl = twitter_url,
            linkedin = linkedin,
            linkedinUrl = linkedin_url,
            faqLink = faq_url,
            codeOfConductLink = coc_url
        )
    }
    private val partnerMapper = { name: String, _: String, _: String, logo_url: String, site_url: String? ->
        PartnerItemUi(logoUrl = logo_url, siteUrl = site_url, name = name)
    }

    fun fetchEvent(): EventUi = db.transactionWithResult {
        val event = db.eventQueries.selectEvent(eventId, eventMapper).executeAsOne()
        val golds = db.eventQueries.selectPartners("gold", eventId, partnerMapper).executeAsList()
        val silvers = db.eventQueries.selectPartners("silver", eventId, partnerMapper).executeAsList()
        val bronzes = db.eventQueries.selectPartners("bronze", eventId, partnerMapper).executeAsList()
        val others = db.eventQueries.selectPartners("other", eventId, partnerMapper).executeAsList()
        return@transactionWithResult EventUi(
            eventInfo = event,
            partners = PartnerGroupsUi(
                golds = golds,
                silvers = silvers,
                bronzes = bronzes,
                others = others
            )
        )
    }

    fun insertEvent(event: Event) = db.transaction {
        val eventDb = event.convertToModelDb()
        db.eventQueries.insertEvent(
            id = eventDb.id,
            name = eventDb.name,
            address = eventDb.address,
            date = eventDb.date,
            twitter = eventDb.twitter,
            twitter_url = eventDb.twitter_url,
            linkedin = eventDb.linkedin,
            linkedin_url = eventDb.linkedin_url,
            faq_url = eventDb.faq_url,
            coc_url = eventDb.coc_url
        )
        event.partners.golds.forEach {
            db.eventQueries.insertPartner(it.name, event.id, type = "gold", it.logoUrl, it.siteUrl)
        }
        event.partners.silvers.forEach {
            db.eventQueries.insertPartner(it.name, event.id, type = "silver", it.logoUrl, it.siteUrl)
        }
        event.partners.bronzes.forEach {
            db.eventQueries.insertPartner(it.name, event.id, type = "bronze", it.logoUrl, it.siteUrl)
        }
        event.partners.others.forEach {
            db.eventQueries.insertPartner(it.name, event.id, type = "other", it.logoUrl, it.siteUrl)
        }
    }
}