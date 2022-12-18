package org.gdglille.devfest.models

data class PartnerGroupUi(
    val type: String,
    val partners: List<List<PartnerItemUi>>
) {
    companion object {
        val fake = PartnerGroupUi(
            type = "Gold",
            partners = arrayListOf(arrayListOf(PartnerItemUi.fake, PartnerItemUi.fake, PartnerItemUi.fake))
        )
    }
}
