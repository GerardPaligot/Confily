package org.gdglille.devfest.models

data class PartnerGroupsUi(
    val groups: List<PartnerGroupUi>
) {
    companion object {
        val fake = PartnerGroupsUi(
            groups = listOf(
                PartnerGroupUi(
                    type = "Gold",
                    partners = arrayListOf(arrayListOf(PartnerItemUi.fake, PartnerItemUi.fake, PartnerItemUi.fake))
                ),
                PartnerGroupUi(
                    type = "Silver",
                    partners = arrayListOf(arrayListOf(PartnerItemUi.fake, PartnerItemUi.fake, PartnerItemUi.fake))
                ),
                PartnerGroupUi(
                    type = "Bronze",
                    partners = arrayListOf(arrayListOf(PartnerItemUi.fake, PartnerItemUi.fake, PartnerItemUi.fake))
                )
            )
        )
    }
}
