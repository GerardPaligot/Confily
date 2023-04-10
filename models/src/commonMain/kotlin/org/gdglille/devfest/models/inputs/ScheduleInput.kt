package org.gdglille.devfest.models.inputs

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleInput(
    val order: Int,
    val startTime: String,
    val endTime: String? = null,
    val room: String,
    @SerialName("talk_id")
    val talkId: String? = null
) : Validator {
    override fun validate(): List<String> {
        val errors = arrayListOf<String>()
        val timePattern = "(\\d{4}-\\d{2}-\\d{2})[A-Z]+(\\d{2}:\\d{2}:\\d{2}).[0-9]{3}"
        if (order < 0) errors.add("Order should be >= 0")
        if (!startTime.matches(Regex(timePattern))) errors.add("Your start time should be formatted with this regex $timePattern")
        if (endTime != null && !endTime.matches(Regex(timePattern))) errors.add("Your end time should be formatted with this regex $timePattern")
        if (talkId == null && endTime == null) errors.add("You must give the end time when the schedule is a pause")
        return errors
    }
}
