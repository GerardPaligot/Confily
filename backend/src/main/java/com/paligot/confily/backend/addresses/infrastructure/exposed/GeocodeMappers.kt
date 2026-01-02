package com.paligot.confily.backend.addresses.infrastructure.exposed

import com.paligot.confily.backend.addresses.infrastructure.provider.Geocode

fun Geocode.toEntity(): AddressEntity? {
    if (this.results.isEmpty()) return null
    val result = this.results.first()
    val streetNumber = result.addressComponents.find { it.types.contains("street_number") }?.longName ?: ""
    val route = result.addressComponents.find { it.types.contains("route") }?.longName ?: ""
    return AddressEntity.new {
        this.street = "$streetNumber $route".trim()
        this.postalCode = result.addressComponents.find { it.types.contains("postal_code") }?.longName ?: ""
        this.city = result.addressComponents.find { it.types.contains("locality") }?.longName ?: ""
        this.country = result.addressComponents.find { it.types.contains("country") }?.longName ?: ""
        this.countryCode = result.addressComponents.find { it.types.contains("country") }?.shortName ?: ""
        this.latitude = result.geometry.location.lat.toBigDecimal()
        this.longitude = result.geometry.location.lng.toBigDecimal()
    }
}
