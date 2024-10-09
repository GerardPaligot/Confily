package com.paligot.confily.wear.partners.panes

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap

@Immutable
data class PartnersModelUi(
    val partners: ImmutableMap<String, ImmutableList<PartnerModelUi>>
)

@Immutable
data class PartnerModelUi(
    val id: String,
    val name: String,
    val url: String
)
