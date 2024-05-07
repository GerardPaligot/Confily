package org.gdglille.devfest.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class PlanningItem {
    abstract val id: String
    abstract val order: Int

    @Serializable
    @SerialName("talk-session")
    data class TalkItem(
        override val id: String,
        override val order: Int,
        val startTime: String,
        val endTime: String,
        val room: String,
        val talk: Talk
    ) : PlanningItem()

    @Serializable
    @SerialName("event-session")
    data class EventItem(
        override val id: String,
        override val order: Int,
        val startTime: String,
        val endTime: String,
        val room: String,
        val info: Info
    ) : PlanningItem()
}

@Serializable
data class Info(
    val id: String,
    val title: String,
    val description: String?
)
