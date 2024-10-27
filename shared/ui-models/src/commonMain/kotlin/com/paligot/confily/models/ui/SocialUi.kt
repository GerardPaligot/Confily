package com.paligot.confily.models.ui

data class SocialUi(
    val type: SocialTypeUi?,
    val url: String
)

enum class SocialTypeUi {
    LinkedIn,
    X,
    Mastodon,
    Bluesky,
    Facebook,
    Instagram,
    YouTube,
    GitHub,
    Email,
    Website
}
