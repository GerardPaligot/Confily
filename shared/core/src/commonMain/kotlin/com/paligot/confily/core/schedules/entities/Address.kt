package com.paligot.confily.core.schedules.entities

import com.paligot.confily.schedules.ui.models.AddressUi
import kotlinx.collections.immutable.toImmutableList
import kotlin.native.ObjCName

@ObjCName("AddressEntity")
class Address(
    val formatted: List<String>,
    val latitude: Double,
    val longitude: Double
)

fun Address.mapToAddressUi(): AddressUi = AddressUi(
    formattedAddress = formatted.toImmutableList(),
    address = formatted.joinToString(", "),
    latitude = latitude,
    longitude = longitude
)
