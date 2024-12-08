package com.paligot.confily.models.ui

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class EventInfoUi(
    val name: String,
    val formattedAddress: ImmutableList<String>,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val date: String,
    val socials: ImmutableList<SocialUi>,
    val faqLink: String,
    val codeOfConductLink: String
) {
    companion object {
        val fake = EventInfoUi(
            name = "Devfest Lille",
            formattedAddress = persistentListOf(
                "Kinepolis",
                "Rue du Château d'Isenghien",
                "Lille",
                "France"
            ),
            address = "1 Rue du Château d'Isenghien, 59160 Lille",
            latitude = 50.6526513,
            longitude = 2.9826465,
            date = "June 9th 2022",
            socials = persistentListOf(
                SocialUi(type = SocialTypeUi.X, url = "https://twitter.com/DevfestLille"),
                SocialUi(
                    type = SocialTypeUi.LinkedIn,
                    url = "https://www.linkedin.com/company/gdg-lille/"
                )
            ),
            faqLink = "https://devfest.gdglille.org/faq/",
            codeOfConductLink = "https://devfest.gdglille.org/code-conduite/"
        )
    }
}
