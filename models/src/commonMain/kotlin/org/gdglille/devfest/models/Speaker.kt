package org.gdglille.devfest.models

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
    val website: String?,
    val twitter: String?,
    val mastodon: String?,
    val github: String?,
    val linkedin: String?
)
