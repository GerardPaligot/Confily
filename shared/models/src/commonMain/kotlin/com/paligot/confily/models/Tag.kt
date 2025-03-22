package com.paligot.confily.models

import kotlinx.serialization.Serializable

@Serializable
data class Tag(
    val id: String,
    val name: String
)
