package com.paligot.confily.backend.addresses.infrastructure.exposed

import com.paligot.confily.models.Address

fun AddressEntity.toModel(): Address = Address(
    formatted = listOf(this.street, this.city, this.postalCode, this.country),
    address = this.street,
    country = this.country,
    countryCode = this.countryCode,
    city = this.city,
    lat = this.latitude?.toDouble() ?: 0.0,
    lng = this.longitude?.toDouble() ?: 0.0
)
