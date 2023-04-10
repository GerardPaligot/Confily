package org.gdglille.devfest.backend.schedulers

data class ScheduleDb(
    val order: Int? = 0,
    val startTime: String = "",
    val endTime: String = "",
    val room: String = "",
    val talkId: String? = null
) {
    val id: String = talkId ?: "$startTime-pause"
}
