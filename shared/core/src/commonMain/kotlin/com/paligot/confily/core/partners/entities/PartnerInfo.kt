package com.paligot.confily.core.partners.entities

import com.paligot.confily.core.schedules.entities.Address
import kotlin.native.ObjCName

@ObjCName("PartnerInfoEntity")
class PartnerInfo(
    val id: String,
    val name: String,
    val description: String,
    val logoUrl: String,
    val address: Address?
)
