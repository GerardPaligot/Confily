package org.gdglille.devfest.models

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val name: String,
    val color: String,
    val icon: String
)
