package org.gdglille.devfest.models

data class ScaffoldConfigUi(
    val hasNetworking: Boolean = false,
    val hasSpeakerList: Boolean = false,
    val hasPartnerList: Boolean = false,
    val hasMenus: Boolean = false,
    val hasQAndA: Boolean = false,
    val hasBilletWebTicket: Boolean = false,
    val hasProfile: Boolean = false,
    val agendaTabs: List<String> = emptyList(),
    val hasUsersInNetworking: Boolean = false
)
