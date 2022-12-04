package org.gdglille.devfest.backend.internals.network.geolocation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddressComponent(
    @SerialName("long_name")
    val longName: String,
    @SerialName("short_name")
    val shortName: String,
    val types: List<String>
)

@Serializable
data class Geometry(
    val location: Location
)

@Serializable
data class Location(
    val lat: Double,
    val lng: Double
)

@Serializable
data class Result(
    @SerialName("address_components")
    val addressComponents: List<AddressComponent>,
    @SerialName("formatted_address")
    val formattedAddress: String,
    val geometry: Geometry,
    @SerialName("place_id")
    val placeId: String,
    val types: List<String>
)

@Serializable
data class Geocode(
    val results: List<Result>,
    val status: String
)
