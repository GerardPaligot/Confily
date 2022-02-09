package com.paligot.conferences.models

data class PartnerGroupsUi(
    val golds: List<PartnerItemUi>,
    val silvers: List<PartnerItemUi>,
    val bronzes: List<PartnerItemUi>,
    val others: List<PartnerItemUi>,
)