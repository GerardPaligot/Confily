package com.paligot.confily.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Speaker(
    val id: String,
    @SerialName("display_name")
    val displayName: String,
    val pronouns: String?,
    val bio: String,
    @SerialName("job_title")
    val jobTitle: String?,
    val company: String?,
    @SerialName("photo_url")
    val photoUrl: String,
    val socials: List<SocialItem>,
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
