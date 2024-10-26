package com.paligot.confily.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SocialItem(
    val type: SocialType,
    val url: String
)

enum class SocialType {
    @SerialName("linkedin")
    LinkedIn,

    @SerialName("x")
    X,

    @SerialName("mastodon")
    Mastodon,

    @SerialName("bluesky")
    Bluesky,

    @SerialName("facebook")
    Facebook,

    @SerialName("instagram")
    Instagram,

    @SerialName("youtube")
    YouTube,

    @SerialName("github")
    GitHub,

    @SerialName("email")
    Email,

    @SerialName("website")
    Website
}
