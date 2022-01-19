package com.paligot.conferences.android.screens.event

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.paligot.conferences.android.R
import com.paligot.conferences.android.components.events.*
import com.paligot.conferences.android.theme.Conferences4HallTheme
import com.paligot.conferences.repositories.AgendaRepository

data class EventUi(
    val eventInfo: EventInfoUi,
    val partners: PartnerGroupsUi
)

data class PartnerGroupsUi(
    val golds: List<PartnerItemUi>,
    val silvers: List<PartnerItemUi>,
    val bronzes: List<PartnerItemUi>,
    val others: List<PartnerItemUi>,
)

val event = EventUi(
    eventInfo = EventInfoUi(
        logo = R.drawable.ic_launcher_foreground,
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
        golds = arrayListOf(partnerUi),
        silvers = arrayListOf(partnerUi, partnerUi, partnerUi, partnerUi),
        bronzes = arrayListOf(partnerUi),
        others = emptyList()
    )
)

@Composable
fun EventVM(
    agendaRepository: AgendaRepository,
    modifier: Modifier = Modifier,
    onFaqClick: (url: String) -> Unit,
    onCoCClick: (url: String) -> Unit,
    onTwitterClick: (url: String) -> Unit,
    onLinkedInClick: (url: String) -> Unit,
    onPartnerClick: (siteUrl: String) -> Unit
) {
    val viewModel: EventViewModel = viewModel(
        factory = EventViewModel.Factory.create(agendaRepository)
    )
    val uiState = viewModel.uiState.collectAsState()
    when (uiState.value) {
        is EventUiState.Loading -> Text("Loading...")
        is EventUiState.Failure -> Text("Something wrong happened")
        is EventUiState.Success -> Event(
            event = (uiState.value as EventUiState.Success).event,
            modifier = modifier,
            onFaqClick = onFaqClick,
            onCoCClick = onCoCClick,
            onTwitterClick = onTwitterClick,
            onLinkedInClick = onLinkedInClick,
            onPartnerClick = onPartnerClick
        )
    }
}

@Composable
fun Event(
    event: EventUi,
    modifier: Modifier = Modifier,
    onFaqClick: (url: String) -> Unit,
    onCoCClick: (url: String) -> Unit,
    onTwitterClick: (url: String) -> Unit,
    onLinkedInClick: (url: String) -> Unit,
    onPartnerClick: (siteUrl: String) -> Unit
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 8.dp),
        contentPadding = PaddingValues(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            EventSection(
                eventInfo = event.eventInfo,
                onFaqClick = onFaqClick,
                onCoCClick = onCoCClick,
                onTwitterClick = onTwitterClick,
                onLinkedInClick = onLinkedInClick
            )
        }
        item { PartnerDivider(title = "Gold") }
        items(event.partners.golds.chunked(3)) {
            PartnerRow(partners = it, onPartnerClick = onPartnerClick)
        }
        item { PartnerDivider(title = "Silver") }
        items(event.partners.silvers.chunked(3)) {
            PartnerRow(partners = it, onPartnerClick = onPartnerClick)
        }
        item { PartnerDivider(title = "Bronze") }
        items(event.partners.bronzes.chunked(3)) {
            PartnerRow(partners = it, onPartnerClick = onPartnerClick)
        }
    }
}

@Preview
@Composable
fun Eventreview() {
    Conferences4HallTheme {
        Scaffold {
            Event(
                event = event,
                onFaqClick = {},
                onCoCClick = {},
                onTwitterClick = {},
                onLinkedInClick = {},
                onPartnerClick = {}
            )
        }
    }
}
