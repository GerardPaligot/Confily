package com.paligot.confily.models

import kotlinx.serialization.Serializable

@Serializable
data class CreatedMap(
    val id: String,
    val url: String
)
