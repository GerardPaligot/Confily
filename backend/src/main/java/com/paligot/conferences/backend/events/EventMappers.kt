package com.paligot.conferences.backend.events

import com.paligot.conferences.backend.network.Event
import com.paligot.conferences.models.EventAddress

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

fun EventDb.convertToModel() = com.paligot.conferences.models.Event(
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
    endDate = this.endDate
)
