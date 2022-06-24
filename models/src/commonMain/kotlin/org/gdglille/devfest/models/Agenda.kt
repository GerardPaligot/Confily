package org.gdglille.devfest.models

import kotlinx.serialization.Serializable

@Serializable
data class Agenda(
    val talks: Map<String, List<ScheduleItem>>
)
