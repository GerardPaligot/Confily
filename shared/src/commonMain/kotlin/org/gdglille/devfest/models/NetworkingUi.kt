package org.gdglille.devfest.models

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class NetworkingUi(
    val userProfileUi: UserProfileUi?,
    val showQrCode: Boolean,
    val users: ImmutableList<UserNetworkingUi> = persistentListOf(),
) {
    companion object {
        val fake = NetworkingUi(
            userProfileUi = UserProfileUi.fake,
            showQrCode = false,
            users = persistentListOf(
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
