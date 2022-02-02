package com.paligot.conferences.models.inputs

import kotlinx.serialization.Serializable

@Serializable
data class UserEmailInput(
    val email: String
)
