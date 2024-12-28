package com.paligot.confily.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamMember(
    val id: String,
    @SerialName("display_name")
    val displayName: String,
    val bio: String,
    val role: String?,
    @SerialName("photo_url")
    val photoUrl: String?,
    val socials: List<SocialItem>
)
