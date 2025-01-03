package com.paligot.confily.partners.ui.models

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class PartnerGroupUi(
    val type: String,
    val partners: ImmutableList<PartnerItemUi>
) {
    companion object {
        val fake = PartnerGroupUi(
            type = "Gold",
            partners = persistentListOf(
                PartnerItemUi.fake.copy(id = "1"),
                PartnerItemUi.fake.copy(id = "2"),
                PartnerItemUi.fake.copy(id = "3")
            )
        )
    }
}
