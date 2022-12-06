package org.gdglille.devfest.models

data class PartnerGroupsUi(
    val map: Map<String, List<List<PartnerItemUi>>>
) {
    companion object {
        val fake = PartnerGroupsUi(
            map = mapOf(
                "Gold" to arrayListOf(arrayListOf(PartnerItemUi.fake, PartnerItemUi.fake, PartnerItemUi.fake)),
                "Silver" to arrayListOf(arrayListOf(PartnerItemUi.fake, PartnerItemUi.fake, PartnerItemUi.fake)),
                "Bronze" to arrayListOf(arrayListOf(PartnerItemUi.fake, PartnerItemUi.fake, PartnerItemUi.fake))
            )
        )
    }
}
