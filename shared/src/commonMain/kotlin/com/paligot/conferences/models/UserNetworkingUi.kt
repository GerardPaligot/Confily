package com.paligot.conferences.models

import kotlinx.serialization.Serializable

@Serializable
data class UserNetworkingUi(
    val email: String,
    val firstName: String,
    val lastName: String,
    val company: String
)
