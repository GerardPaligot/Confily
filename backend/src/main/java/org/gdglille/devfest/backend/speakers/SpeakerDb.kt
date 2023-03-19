package org.gdglille.devfest.backend.speakers

data class SpeakerDb(
    val id: String = "",
    val displayName: String = "",
    val pronouns: String? = null,
    val bio: String = "",
    val jobTitle: String? = null,
    val company: String? = null,
    val photoUrl: String = "",
    val website: String? = null,
    val twitter: String? = null,
    val mastodon: String? = null,
    val github: String? = null,
    val linkedin: String? = null
)
