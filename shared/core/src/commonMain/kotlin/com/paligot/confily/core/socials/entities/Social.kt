package com.paligot.confily.core.socials.entities

import com.paligot.confily.models.ui.SocialTypeUi
import com.paligot.confily.models.ui.SocialUi
import kotlin.native.ObjCName

@ObjCName("SocialEntity")
class Social(
    val url: String,
    val type: String
)

fun Social.mapToSocialUi(): SocialUi = SocialUi(
    url = url,
    type = when (type) {
        "linkedin" -> SocialTypeUi.LinkedIn
        "x" -> SocialTypeUi.X
        "twitter" -> SocialTypeUi.X
        "mastodon" -> SocialTypeUi.Mastodon
        "bluesky" -> SocialTypeUi.Bluesky
        "facebook" -> SocialTypeUi.Facebook
        "instagram" -> SocialTypeUi.Instagram
        "youtube" -> SocialTypeUi.YouTube
        "github" -> SocialTypeUi.GitHub
        "email" -> SocialTypeUi.Email
        "website" -> SocialTypeUi.Website
        else -> null
    }
)
