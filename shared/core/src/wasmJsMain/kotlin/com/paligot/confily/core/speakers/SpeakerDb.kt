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
    val eventId: String
)
