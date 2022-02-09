package com.paligot.conferences.models

import com.paligot.conferences.Image

data class UserProfileUi(
    val email: String,
    val hasQrCode: Boolean,
    val showQrCode: Boolean,
    val emails: List<String> = emptyList(),
    val qrcode: Image?
) {
    companion object {
        val fake = UserProfileUi(
            email = "email@fake.com",
            hasQrCode = true,
            showQrCode = false,
            emails = arrayListOf(
                "gerard@gdglille.org",
                "gerard@gdglille.org",
                "gerard@gdglille.org",
                "gerard@gdglille.org",
                "gerard@gdglille.org",
            ),
            qrcode = null
        )
    }
}
