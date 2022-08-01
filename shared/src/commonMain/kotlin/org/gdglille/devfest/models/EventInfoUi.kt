package org.gdglille.devfest.models

data class EventInfoUi(
    val name: String,
    val address: String,
    val date: String,
    val twitter: String?,
    val twitterUrl: String?,
    val linkedin: String?,
    val linkedinUrl: String?,
    val faqLink: String,
    val codeOfConductLink: String,
) {
    companion object {
        val fake = EventInfoUi(
            name = "Devfest Lille",
            address = "1 Rue du Château d'Isenghien, 59160 Lille",
            date = "June 9th 2022",
            twitter = "DevfestLille",
            twitterUrl = "https://twitter.com/DevfestLille",
            linkedin = "gdg-lille",
            linkedinUrl = "https://www.linkedin.com/company/gdg-lille/",
            faqLink = "https://devfest.gdglille.org/faq/",
            codeOfConductLink = "https://devfest.gdglille.org/code-conduite/"
        )
    }
}
