package com.paligot.conferences.models

data class NetworkingUi(
    val userProfileUi: UserProfileUi,
    val showQrCode: Boolean,
    val emails: List<String> = emptyList(),
) {
    companion object {
        val fake = NetworkingUi(
            userProfileUi = UserProfileUi.fake,
            showQrCode = false,
            emails = arrayListOf(
                "gerard@gdglille.org",
                "gerard@gdglille.org",
                "gerard@gdglille.org",
                "gerard@gdglille.org",
                "gerard@gdglille.org",
            ),
        )
    }
}
