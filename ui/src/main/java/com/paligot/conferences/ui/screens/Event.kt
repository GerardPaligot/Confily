package com.paligot.conferences.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.paligot.conferences.repositories.EventInfoUi
import com.paligot.conferences.repositories.EventUi
import com.paligot.conferences.repositories.PartnerGroupsUi
import com.paligot.conferences.ui.components.events.EventSection
import com.paligot.conferences.ui.components.events.PartnerDivider
import com.paligot.conferences.ui.components.events.PartnerRow
import com.paligot.conferences.ui.components.events.partnerUi

val fakeEvent = EventUi(
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
        golds = arrayListOf(partnerUi, partnerUi, partnerUi),
        silvers = arrayListOf(partnerUi, partnerUi, partnerUi),
        bronzes = arrayListOf(partnerUi, partnerUi, partnerUi),
        others = emptyList()
    )
)

@Composable
fun Event(
    logo: Int,
    event: EventUi,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    onFaqClick: (url: String) -> Unit,
    onCoCClick: (url: String) -> Unit,
    onTwitterClick: (url: String?) -> Unit,
    onLinkedInClick: (url: String?) -> Unit,
    onPartnerClick: (siteUrl: String?) -> Unit
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 8.dp),
        contentPadding = PaddingValues(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            EventSection(
                logo = logo,
                eventInfo = event.eventInfo,
                isLoading = isLoading,
                onFaqClick = onFaqClick,
                onCoCClick = onCoCClick,
                onTwitterClick = onTwitterClick,
                onLinkedInClick = onLinkedInClick
            )
        }
        item { PartnerDivider(title = "Gold") }
        items(event.partners.golds.chunked(3)) {
            PartnerRow(partners = it, onPartnerClick = onPartnerClick, isLoading = isLoading)
        }
        item { PartnerDivider(title = "Silver") }
        items(event.partners.silvers.chunked(3)) {
            PartnerRow(partners = it, onPartnerClick = onPartnerClick, isLoading = isLoading)
        }
        item { PartnerDivider(title = "Bronze") }
        items(event.partners.bronzes.chunked(3)) {
            PartnerRow(partners = it, onPartnerClick = onPartnerClick, isLoading = isLoading)
        }
    }
}
