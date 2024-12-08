package com.paligot.confily.models.inputs

import com.paligot.confily.models.SocialType
import kotlinx.serialization.Serializable

@Serializable
data class SocialInput(
    val type: SocialType,
    val url: String
)
