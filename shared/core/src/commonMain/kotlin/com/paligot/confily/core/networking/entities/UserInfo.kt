package com.paligot.confily.core.networking.entities

import kotlin.native.ObjCName

@ObjCName("UserEntity")
class UserInfo(
    val firstName: String,
    val lastName: String,
    val email: String,
    val company: String?,
    val qrCode: ByteArray?
) {
    fun copy(
        firstName: String = this.firstName,
        lastName: String = this.lastName,
        email: String = this.email,
        company: String? = this.company,
        qrCode: ByteArray? = this.qrCode
    ) = UserInfo(firstName, lastName, email, company, qrCode)
}
