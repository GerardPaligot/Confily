package org.gdglille.devfest.models

data class PartnerGroupsUi(
    val golds: List<List<PartnerItemUi>>,
    val silvers: List<List<PartnerItemUi>>,
    val bronzes: List<List<PartnerItemUi>>,
    val others: List<List<PartnerItemUi>>,
)