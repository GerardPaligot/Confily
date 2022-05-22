package org.gdglille.devfest.backend.events

import org.gdglille.devfest.backend.network.conferencehall.Event
import org.gdglille.devfest.backend.partners.PartnerDb
import org.gdglille.devfest.backend.partners.convertToModel
import org.gdglille.devfest.models.EventAddress
import org.gdglille.devfest.models.EventLunchMenu
import org.gdglille.devfest.models.EventPartners
import org.gdglille.devfest.models.inputs.BilletWebConfigInput
import org.gdglille.devfest.models.inputs.EventInput
import org.gdglille.devfest.models.inputs.LunchMenuInput

fun Event.convertToDb(year: String, eventId: String, apiKey: String) = EventDb(
    year = year,
    conferenceHallId = eventId,
    apiKey = apiKey,
    name = this.name,
    address = EventAddressDb(
        address = this.address.formattedAddress,
        country = this.address.country.longName,
        countryCode = this.address.country.shortName,
        city = this.address.locality.longName,
        lat = this.address.latLng.lat,
        lng = this.address.latLng.lng
    ),
    startDate = this.conferenceDates.start,
    endDate = this.conferenceDates.end
)

fun LunchMenuDb.convertToModel() = EventLunchMenu(
    name = name,
    dish = dish,
    accompaniment = accompaniment,
    dessert = dessert
)

fun EventDb.convertToModel(
    golds: List<PartnerDb>,
    silvers: List<PartnerDb>,
    bronzes: List<PartnerDb>,
    others: List<PartnerDb>
) = org.gdglille.devfest.models.Event(
    id = this.year,
    name = this.name,
    address = EventAddress(
        address = this.address.address,
        country = this.address.country,
        countryCode = this.address.countryCode,
        city = this.address.city,
        lat = this.address.lat,
        lng = this.address.lng
    ),
    startDate = this.startDate,
    endDate = this.endDate,
    partners = EventPartners(
        golds = golds.map { it.convertToModel() },
        silvers = silvers.map { it.convertToModel() },
        bronzes = bronzes.map { it.convertToModel() },
        others = others.map { it.convertToModel() }
    ),
    menus = menus.map { it.convertToModel() },
    twitterUrl = this.twitterUrl,
    linkedinUrl = this.linkedinUrl,
    faqLink = this.faqLink,
    codeOfConductLink = this.codeOfConductLink,
    updatedAt = this.updatedAt
)

fun LunchMenuInput.convertToDb() = LunchMenuDb(
    name = name,
    dish = dish,
    accompaniment = accompaniment,
    dessert = dessert
)

fun BilletWebConfigInput.convertToDb() = BilletWebConfigurationDb(
    eventId = eventId,
    userId = userId,
    apiKey = apiKey
)

fun EventInput.convertToDb(event: EventDb, openFeedbackId: String?, apiKey: String) = EventDb(
    year = event.year,
    conferenceHallId = event.conferenceHallId,
    openFeedbackId = openFeedbackId ?: event.openFeedbackId,
    billetWebConfig = this.billetWebConfig?.convertToDb(),
    apiKey = apiKey,
    name = this.name,
    address = EventAddressDb(
        address = this.address.address,
        country = this.address.country,
        countryCode = this.address.countryCode,
        city = this.address.city,
        lat = this.address.lat,
        lng = this.address.lng
    ),
    menus = menus.map { it.convertToDb() },
    startDate = this.startDate,
    endDate = this.endDate,
    formats = this.formats,
    twitterUrl = this.twitterUrl,
    linkedinUrl = this.linkedinUrl,
    faqLink = this.faqLink,
    codeOfConductLink = this.codeOfConductLink,
    updatedAt = this.updatedAt
)
