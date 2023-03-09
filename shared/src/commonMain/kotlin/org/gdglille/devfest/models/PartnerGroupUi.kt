package org.gdglille.devfest.models

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class PartnerGroupUi(
    val type: String,
    val partners: ImmutableList<ImmutableList<PartnerItemUi>>
) {
    companion object {
        val fake = PartnerGroupUi(
            type = "Gold",
            partners = persistentListOf(
                persistentListOf(
                    PartnerItemUi.fake,
                    PartnerItemUi.fake,
                    PartnerItemUi.fake
                )
            )
        )
    }
}
