package org.gdglille.devfest.backend.events

import org.gdglille.devfest.backend.network.Event
import org.gdglille.devfest.backend.partners.PartnerDb
import org.gdglille.devfest.backend.partners.convertToModel
import org.gdglille.devfest.models.EventAddress
import org.gdglille.devfest.models.EventPartners
import org.gdglille.devfest.models.inputs.EventInput

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
    twitterUrl = this.twitterUrl,
    linkedinUrl = this.linkedinUrl,
    faqLink = this.faqLink,
    codeOfConductLink = this.codeOfConductLink,
    updatedAt = this.updatedAt
)

fun EventInput.convertToDb(event: EventDb, openFeedbackId: String?, apiKey: String) = EventDb(
    year = event.year,
    conferenceHallId = event.conferenceHallId,
    openFeedbackId = openFeedbackId ?: event.openFeedbackId,
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
    startDate = this.startDate,
    endDate = this.endDate,
    formats = this.formats,
    twitterUrl = this.twitterUrl,
    linkedinUrl = this.linkedinUrl,
    faqLink = this.faqLink,
    codeOfConductLink = this.codeOfConductLink,
    updatedAt = this.updatedAt
)
