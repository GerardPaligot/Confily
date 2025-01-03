package com.paligot.confily.partners.ui.models

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class PartnerGroupsUi(
    val groups: ImmutableList<PartnerGroupUi>
) {
    companion object {
        val fake = PartnerGroupsUi(
            groups = persistentListOf(
                PartnerGroupUi(
                    type = "Gold",
                    partners = persistentListOf(
                        PartnerItemUi.fake.copy(id = "1"),
                        PartnerItemUi.fake.copy(id = "2"),
                        PartnerItemUi.fake.copy(id = "3")
                    )
                ),
                PartnerGroupUi(
                    type = "Silver",
                    partners = persistentListOf(
                        PartnerItemUi.fake.copy(id = "4"),
                        PartnerItemUi.fake.copy(id = "5"),
                        PartnerItemUi.fake.copy(id = "6")
                    )
                ),
                PartnerGroupUi(
                    type = "Bronze",
                    partners = persistentListOf(
                        PartnerItemUi.fake.copy(id = "7"),
                        PartnerItemUi.fake.copy(id = "8"),
                        PartnerItemUi.fake.copy(id = "9")
                    )
                )
            )
        )
    }
}
