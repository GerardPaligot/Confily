package org.gdglille.devfest.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class Session {
    @Serializable
    @SerialName("talk-session")
    data class Talk(
        val id: String,
        val title: String,
        val level: String?,
        val abstract: String,
        val categoryId: String,
        val formatId: String,
        val language: String,
        val speakers: List<String>,
        @SerialName("link_slides")
        val linkSlides: String?,
        @SerialName("link_replay")
        val linkReplay: String?,
        @SerialName("open_feedback")
        val openFeedback: String?
    ) : Session()

    @Serializable
    @SerialName("event-session")
    data class Event(
        val id: String,
        val title: String,
        val description: String?
    ) : Session()
}
