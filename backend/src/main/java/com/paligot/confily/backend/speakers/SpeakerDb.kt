package com.paligot.confily.backend.speakers

import com.paligot.confily.backend.internals.socials.SocialDb

data class SpeakerDb(
    val id: String = "",
    val displayName: String = "",
    val pronouns: String? = null,
    val bio: String = "",
    val email: String? = null,
    val jobTitle: String? = null,
    val company: String? = null,
    val photoUrl: String = "",
    val socials: List<SocialDb> = emptyList(),
    @Deprecated("use socials property instead")
    val website: String? = null,
    @Deprecated("use socials property instead")
    val twitter: String? = null,
    @Deprecated("use socials property instead")
    val mastodon: String? = null,
    @Deprecated("use socials property instead")
    val github: String? = null,
    @Deprecated("use socials property instead")
    val linkedin: String? = null
)
