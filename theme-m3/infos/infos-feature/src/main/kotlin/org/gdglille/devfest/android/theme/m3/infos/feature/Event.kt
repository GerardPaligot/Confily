package org.gdglille.devfest.android.theme.m3.infos.feature

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.shared.resources.Resource
import org.gdglille.devfest.android.shared.resources.title_plan
import org.gdglille.devfest.android.shared.resources.title_ticket
import org.gdglille.devfest.android.theme.m3.infos.ui.tickets.TicketDetailed
import org.gdglille.devfest.android.theme.m3.infos.ui.tickets.TicketQrCode
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.android.theme.m3.style.events.cards.AddressCard
import org.gdglille.devfest.android.theme.m3.style.events.socials.SocialsSection
import org.gdglille.devfest.models.ui.EventUi
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun Event(
    event: EventUi,
    onLinkClicked: (url: String) -> Unit,
    onItineraryClicked: (lat: Double, lng: Double) -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            SocialsSection(
                title = event.eventInfo.name,
                pronouns = null,
                subtitle = event.eventInfo.date,
                onLinkClicked = onLinkClicked,
                modifier = Modifier.padding(horizontal = 16.dp),
                isLoading = isLoading,
                twitterUrl = event.eventInfo.twitterUrl,
                linkedinUrl = event.eventInfo.linkedinUrl
            )
        }
        event.ticket?.let {
            item {
                Text(
                    text = stringResource(Resource.string.title_ticket),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 16.dp).semantics { heading() }
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (it.info != null) {
                    TicketDetailed(
                        ticket = it.info!!,
                        qrCode = it.qrCode,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                        isLoading = isLoading
                    )
                } else if (it.qrCode != null) {
                    TicketQrCode(
                        qrCode = it.qrCode!!,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                        isLoading = isLoading
                    )
                }
            }
        }
        item {
            Text(
                text = stringResource(Resource.string.title_plan),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp).semantics { heading() }
            )
            Spacer(modifier = Modifier.height(8.dp))
            AddressCard(
                formattedAddress = event.eventInfo.formattedAddress,
                isLoading = isLoading,
                onItineraryClicked = {
                    onItineraryClicked(event.eventInfo.latitude, event.eventInfo.longitude)
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@ExperimentalFoundationApi
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
private fun EventPreview() {
    Conferences4HallTheme {
        Scaffold {
            Event(
                event = EventUi.fake,
                onLinkClicked = {},
                onItineraryClicked = { _, _ -> }
            )
        }
    }
}
