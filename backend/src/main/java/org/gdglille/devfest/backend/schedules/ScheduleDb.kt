package org.gdglille.devfest.backend.schedules

data class ScheduleDb(
    val order: Int? = 0,
    val startTime: String = "",
    val endTime: String = "",
    val room: String = "",
    val talkId: String? = null
) {
    val id: String = talkId ?: "$startTime-pause"
}
