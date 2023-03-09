package org.gdglille.devfest.models

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
                        persistentListOf(
                            PartnerItemUi.fake,
                            PartnerItemUi.fake,
                            PartnerItemUi.fake
                        )
                    )
                ),
                PartnerGroupUi(
                    type = "Silver",
                    partners = persistentListOf(
                        persistentListOf(
                            PartnerItemUi.fake,
                            PartnerItemUi.fake,
                            PartnerItemUi.fake
                        )
                    )
                ),
                PartnerGroupUi(
                    type = "Bronze",
                    partners = persistentListOf(
                        persistentListOf(
                            PartnerItemUi.fake,
                            PartnerItemUi.fake,
                            PartnerItemUi.fake
                        )
                    )
                )
            )
        )
    }
}
