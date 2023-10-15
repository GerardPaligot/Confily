package org.gdglille.devfest.models

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: String,
    val name: String,
    val color: String,
    val icon: String
)
