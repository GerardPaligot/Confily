package com.paligot.confily.backend.addresses.infrastructure.firestore

data class AddressEntity(
    val formatted: List<String> = emptyList(),
    val address: String = "",
    val country: String = "",
    val countryCode: String = "",
    val city: String = "",
    val lat: Double = 0.0,
    val lng: Double = 0.0
)
