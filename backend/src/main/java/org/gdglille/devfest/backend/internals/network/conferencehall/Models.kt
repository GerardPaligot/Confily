package org.gdglille.devfest.backend.internals.network.conferencehall

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Category(val id: String, val name: String)

@Serializable
data class Format(val id: String, val name: String)

@Serializable
data class Speaker(
    val uid: String,
    val displayName: String,
    val bio: String? = null,
    val company: String? = null,
    val photoURL: String? = null,
    val twitter: String? = null,
    val github: String? = null
)

@Serializable
data class Talk(
    val id: String,
    val title: String,
    val state: String,
    val level: String? = null,
    val abstract: String,
    val categories: String,
    val formats: String,
    val speakers: List<String>,
    val language: String
)

@Serializable
data class Name(
    @SerialName("long_name")
    val longName: String,
    @SerialName("short_name")
    val shortName: String
)

@Serializable
data class LatLng(
    val lat: Double,
    val lng: Double
)

@Serializable
data class Address(
    val country: Name,
    val formattedAddress: String,
    val latLng: LatLng,
    val locality: Name
)

@Serializable
data class ConferenceDate(
    val end: String,
    val start: String
)

@Serializable
data class Event(
    val name: String,
    val address: Address,
    val conferenceDates: ConferenceDate,
    val categories: List<Category>,
    val formats: List<Format>,
    val talks: List<Talk>,
    val speakers: List<Speaker>
)
