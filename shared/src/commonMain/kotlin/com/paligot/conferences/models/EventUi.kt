package com.paligot.conferences.models

data class EventUi(
    val eventInfo: EventInfoUi,
    val partners: PartnerGroupsUi
) {
    companion object {
        val fake = EventUi(
            eventInfo = EventInfoUi(
                name = "Devfest Lille",
                address = "Kinepolis, Rue du Château d'Isenghien, Lille, France",
                date = "9 Juin 2019",
                twitter = "DevfestLille",
                twitterUrl = "https://twitter.com/DevfestLille",
                linkedin = "DevfestLille",
                linkedinUrl = "https://www.linkedin.com/company/18372659/",
                faqLink = "https://devfest.gdglille.org/faq/",
                codeOfConductLink = "https://devfest.gdglille.org/code-conduite/"
            ),
            partners = PartnerGroupsUi(
                golds = arrayListOf(PartnerItemUi.fake, PartnerItemUi.fake, PartnerItemUi.fake),
                silvers = arrayListOf(PartnerItemUi.fake, PartnerItemUi.fake, PartnerItemUi.fake),
                bronzes = arrayListOf(PartnerItemUi.fake, PartnerItemUi.fake, PartnerItemUi.fake),
                others = emptyList()
            )
        )
    }
}
