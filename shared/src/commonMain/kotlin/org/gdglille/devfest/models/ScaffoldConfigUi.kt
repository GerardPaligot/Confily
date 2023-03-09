package org.gdglille.devfest.models

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class ScaffoldConfigUi(
    val hasNetworking: Boolean = false,
    val hasSpeakerList: Boolean = false,
    val hasPartnerList: Boolean = false,
    val hasMenus: Boolean = false,
    val hasQAndA: Boolean = false,
    val hasBilletWebTicket: Boolean = false,
    val hasProfile: Boolean = false,
    val agendaTabs: ImmutableList<String> = persistentListOf(),
    val hasUsersInNetworking: Boolean = false
)
