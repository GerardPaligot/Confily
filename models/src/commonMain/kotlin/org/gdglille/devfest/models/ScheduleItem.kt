package org.gdglille.devfest.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleItem(
    val id: String,
    val order: Int,
    @Deprecated("Replaced by startTime property.")
    val time: String,
    val startTime: String,
    val endTime: String,
    val room: String,
    val talk: Talk?
)

@Serializable
data class ScheduleItemV3(
    val id: String,
    val order: Int,
    val date: String,
    @SerialName("start_time")
    val startTime: String,
    @SerialName("end_time")
    val endTime: String,
    val room: String,
    @SerialName("talk_id")
    val talkId: String?
)
