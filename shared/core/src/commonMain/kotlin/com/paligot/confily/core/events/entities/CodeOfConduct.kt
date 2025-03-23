package com.paligot.confily.core.events.entities

import kotlin.native.ObjCName

@ObjCName("CodeOfConductEntity")
class CodeOfConduct(
    val url: String?,
    val content: String?,
    val phone: String?,
    val email: String?
)
