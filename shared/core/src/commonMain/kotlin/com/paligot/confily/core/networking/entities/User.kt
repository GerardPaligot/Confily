package com.paligot.confily.core.networking.entities

import com.paligot.confily.models.ui.UserProfileUi
import kotlin.native.ObjCName

@ObjCName("UserProfileEntity")
class User(
    val info: UserInfo?,
    val ticket: UserTicket?
)

fun User.mapToUi() = UserProfileUi(
    firstName = info?.firstName ?: ticket?.firstName ?: "",
    lastName = info?.lastName ?: ticket?.lastName ?: "",
    email = info?.email ?: ticket?.email ?: "",
    company = info?.company ?: "",
    qrCode = info?.qrCode
)
