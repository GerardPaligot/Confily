package com.paligot.confily.core.networking.entities

import com.paligot.confily.core.fs.User
import com.paligot.confily.networking.ui.models.UserNetworkingUi
import kotlin.native.ObjCName

@ObjCName("UserItemEntity")
class UserItem(
    val email: String,
    val firstName: String,
    val lastName: String,
    val company: String?
)

fun UserItem.mapToUserNetworkingUi(): UserNetworkingUi = UserNetworkingUi(
    email = email,
    firstName = firstName,
    lastName = lastName,
    company = company ?: ""
)

fun UserItem.mapToFs(): User = User(
    email = email,
    firstname = firstName,
    lastname = lastName,
    company = company ?: ""
)
