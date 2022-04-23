package org.gdglille.devfest.models

import kotlinx.serialization.Serializable

@Serializable
data class OpenFeedback(
    val sessions: Map<String, SessionOF>,
    val speakers: Map<String, SpeakerOF>
)

@Serializable
data class SessionOF(
    val id: String,
    val title: String,
    val trackTitle: String,
    val speakers: List<String>,
    val startTime: String = "",
    val endTime: String = "",
    val tags: List<String> = emptyList(),
)

@Serializable
data class SpeakerOF(
    val id: String,
    val name: String,
    val photoUrl: String? = null,
    val socials: List<SocialOF> = emptyList()
)

@Serializable
data class SocialOF(
    val name: String,
    val link: String
)
