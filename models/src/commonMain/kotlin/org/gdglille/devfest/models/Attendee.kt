package org.gdglille.devfest.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Attendee(
    val id: String,
    @SerialName("ext_id")
    val idExt: String,
    val barcode: String,
    val email: String,
    val firstname: String,
    val name: String
)
