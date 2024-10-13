package com.paligot.confily.core.speakers

import kotlinx.serialization.Serializable

@Serializable
class SpeakerDb(
    val id: String,
    val displayName: String,
    val pronouns: String?,
    val bio: String,
    val jobTitle: String?,
    val company: String?,
    val photoUrl: String,
    val twitter: String?,
    val mastodon: String?,
    val github: String?,
    val linkedin: String?,
    val website: String?,
    val eventId: String
)
