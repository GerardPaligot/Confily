package com.paligot.confily.schedules.ui.models

import kotlinx.collections.immutable.ImmutableList

data class AddressUi(
    val formattedAddress: ImmutableList<String>,
    val address: String,
    val latitude: Double,
    val longitude: Double
)
