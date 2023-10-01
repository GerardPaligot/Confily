package org.gdglille.devfest.models

import kotlinx.serialization.Serializable

@Serializable
data class Agenda(
    val talks: Map<String, List<ScheduleItem>>
)

@Serializable
data class AgendaV3(
    val sessions: List<ScheduleItemV3>,
    val talks: List<TalkV3>,
    val formats: List<Format>,
    val categories: List<Category>,
    val speakers: List<Speaker>
)
