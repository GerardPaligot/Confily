package org.gdglille.devfest.models

data class PartnerGroupsUi(
    val golds: List<List<PartnerItemUi>>,
    val silvers: List<List<PartnerItemUi>>,
    val bronzes: List<List<PartnerItemUi>>,
    val others: List<List<PartnerItemUi>>,
) {
    companion object {
        val fake = PartnerGroupsUi(
            golds = arrayListOf(arrayListOf(PartnerItemUi.fake, PartnerItemUi.fake, PartnerItemUi.fake)),
            silvers = arrayListOf(arrayListOf(PartnerItemUi.fake, PartnerItemUi.fake, PartnerItemUi.fake)),
            bronzes = arrayListOf(arrayListOf(PartnerItemUi.fake, PartnerItemUi.fake, PartnerItemUi.fake)),
            others = emptyList()
        )
    }
}
