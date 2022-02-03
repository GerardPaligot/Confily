package com.paligot.conferences.backend.events

import com.paligot.conferences.backend.network.Event
import com.paligot.conferences.backend.partners.PartnerDb
import com.paligot.conferences.backend.partners.convertToModel
import com.paligot.conferences.models.EventAddress
import com.paligot.conferences.models.EventPartners
import com.paligot.conferences.models.inputs.EventInput

fun Event.convertToDb(id: String) = EventDb(
    id = id,
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
) = com.paligot.conferences.models.Event(
    id = this.id,
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

fun EventInput.convertToDb(eventId: String) = EventDb(
    id = eventId,
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
    twitterUrl = this.twitterUrl,
    linkedinUrl = this.linkedinUrl,
    faqLink = this.faqLink,
    codeOfConductLink = this.codeOfConductLink,
    updatedAt = this.updatedAt
)
