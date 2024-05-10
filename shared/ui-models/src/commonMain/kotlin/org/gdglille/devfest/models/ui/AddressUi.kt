package org.gdglille.devfest.models.ui

import kotlinx.collections.immutable.ImmutableList

data class AddressUi(
    val formattedAddress: ImmutableList<String>,
    val address: String,
    val latitude: Double,
    val longitude: Double
)
