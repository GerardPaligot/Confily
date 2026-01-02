package com.paligot.confily.backend.addresses.infrastructure.firestore

import com.paligot.confily.models.Address

fun AddressEntity.convertToModel() = Address(
    formatted = this.formatted,
    address = this.address,
    country = this.country,
    countryCode = this.countryCode,
    city = this.city,
    lat = this.lat,
    lng = this.lng
)
