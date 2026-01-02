package com.paligot.confily.backend.addresses.infrastructure.firestore

import com.paligot.confily.backend.addresses.infrastructure.provider.Geocode

fun Geocode.convertToEntity(): AddressEntity? {
    if (this.results.isEmpty()) return null
    val result = this.results.first()
    return AddressEntity(
        formatted = result.formattedAddress.split(", "),
        address = result.formattedAddress,
        country = result.addressComponents.find { it.types.contains("country") }?.longName ?: "",
        countryCode = result.addressComponents.find { it.types.contains("country") }?.shortName ?: "",
        city = result.addressComponents.find { it.types.contains("locality") }?.longName ?: "",
        lat = result.geometry.location.lat,
        lng = result.geometry.location.lng
    )
}
