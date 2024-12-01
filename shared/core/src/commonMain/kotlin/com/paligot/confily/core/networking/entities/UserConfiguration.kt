package com.paligot.confily.core.networking.entities

import kotlin.native.ObjCName

@ObjCName("UserConfigurationEntity")
class UserConfiguration(
    val hasProfileCompleted: Boolean,
    val countUsersScanned: Int
)
