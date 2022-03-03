package com.paligot.conferences.models

import com.paligot.conferences.Image
import com.paligot.conferences.toNativeImage

data class UserProfileUi(
    val email: String,
    val firstName: String,
    val lastName: String,
    val company: String,
    val qrCode: Image?
) {
    companion object {
        val fake = UserProfileUi(
            email = "gerard@gdglille.org",
            firstName = "Gérard",
            lastName = "Paligot",
            company = "Decathlon",
            qrCode = null
        )
    }
}
