package com.paligot.confily.backend.third.parties.geocode

import com.paligot.confily.backend.events.AddressDb

fun Geocode.convertToDb(): AddressDb? {
    if (this.results.isEmpty()) return null
    val result = this.results.first()
    return AddressDb(
        formatted = result.formattedAddress.split(", "),
        address = result.formattedAddress,
        country = result.addressComponents.find { it.types.contains("country") }?.longName ?: "",
        countryCode = result.addressComponents.find { it.types.contains("country") }?.shortName ?: "",
        city = result.addressComponents.find { it.types.contains("locality") }?.longName ?: "",
        lat = result.geometry.location.lat,
        lng = result.geometry.location.lng
    )
}
