package com.paligot.conferences.models

import kotlinx.serialization.Serializable

@Serializable
data class EmailQrCode(
    val url: String
)
