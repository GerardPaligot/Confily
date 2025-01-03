package com.paligot.confily.core.partners.entities

import com.paligot.confily.partners.ui.models.PartnerItemUi
import kotlin.native.ObjCName

@ObjCName("PartnerItemEntity")
class PartnerItem(
    val id: String,
    val name: String,
    val logoUrl: String
)

fun PartnerItem.mapToPartnerItemUi() = PartnerItemUi(
    id = id,
    name = name,
    logoUrl = logoUrl
)
