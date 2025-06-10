package com.paligot.confily.backend.internals.application

import com.paligot.confily.backend.internals.infrastructure.firestore.AddressEntity
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
