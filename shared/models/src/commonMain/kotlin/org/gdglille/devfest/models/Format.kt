package org.gdglille.devfest.models

import kotlinx.serialization.Serializable

@Serializable
data class Format(
    val id: String,
    val name: String,
    val time: Int
)
