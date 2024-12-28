package com.paligot.confily.core.events.entities

import kotlin.native.ObjCName

@ObjCName("TeamMemberItemEntity")
class TeamMemberInfo(
    val id: String,
    val displayName: String,
    val bio: String,
    val photoUrl: String?,
    val role: String?
)
