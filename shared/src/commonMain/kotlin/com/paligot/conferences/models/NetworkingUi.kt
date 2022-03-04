package com.paligot.conferences.models

data class NetworkingUi(
    val userProfileUi: UserProfileUi,
    val showQrCode: Boolean,
    val users: List<UserNetworkingUi> = emptyList(),
) {
    companion object {
        val fake = NetworkingUi(
            userProfileUi = UserProfileUi.fake,
            showQrCode = false,
            users = arrayListOf(
                UserNetworkingUi(
                    email = "gerard@gdglille.org",
                    firstName = "Gérard",
                    lastName = "Paligot",
                    company = "Decathlon"
                ),
                UserNetworkingUi(
                    email = "gerard@gdglille.org",
                    firstName = "Gérard",
                    lastName = "Paligot",
                    company = "Decathlon"
                ),
                UserNetworkingUi(
                    email = "gerard@gdglille.org",
                    firstName = "Gérard",
                    lastName = "Paligot",
                    company = "Decathlon"
                ),
                UserNetworkingUi(
                    email = "gerard@gdglille.org",
                    firstName = "Gérard",
                    lastName = "Paligot",
                    company = "Decathlon"
                ),
                UserNetworkingUi(
                    email = "gerard@gdglille.org",
                    firstName = "Gérard",
                    lastName = "Paligot",
                    company = "Decathlon"
                ),
            ),
        )
    }
}
