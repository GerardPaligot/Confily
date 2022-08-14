package org.gdglille.devfest.models

data class ScaffoldConfigUi(
    val hasNetworking: Boolean,
    val hasSpeakerList: Boolean,
    val hasPartnerList: Boolean,
    val hasMenus: Boolean,
    val hasQAndA: Boolean,
    val hasBilletWebTicket: Boolean,
    val hasProfile: Boolean
)
