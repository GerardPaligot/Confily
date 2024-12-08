package com.paligot.confily.infos.panes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.paligot.confily.infos.ui.tickets.TicketDetailed
import com.paligot.confily.infos.ui.tickets.TicketQrCode
import com.paligot.confily.models.ui.EventUi
import com.paligot.confily.models.ui.findBluesky
import com.paligot.confily.models.ui.findEmail
import com.paligot.confily.models.ui.findFacebook
import com.paligot.confily.models.ui.findGitHub
import com.paligot.confily.models.ui.findInstagram
import com.paligot.confily.models.ui.findLinkedIn
import com.paligot.confily.models.ui.findMastodon
import com.paligot.confily.models.ui.findWebsite
import com.paligot.confily.models.ui.findX
import com.paligot.confily.models.ui.findYouTube
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.title_plan
import com.paligot.confily.resources.title_ticket
import com.paligot.confily.style.events.cards.AddressCard
import com.paligot.confily.style.events.socials.SocialsSection
import org.jetbrains.compose.resources.stringResource

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
        contentPadding = PaddingValues(top = 24.dp, bottom = 72.dp),
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
                xUrl = event.eventInfo.socials.findX()?.url,
                mastodonUrl = event.eventInfo.socials.findMastodon()?.url,
                blueskyUrl = event.eventInfo.socials.findBluesky()?.url,
                facebookUrl = event.eventInfo.socials.findFacebook()?.url,
                instagramUrl = event.eventInfo.socials.findInstagram()?.url,
                youtubeUrl = event.eventInfo.socials.findYouTube()?.url,
                githubUrl = event.eventInfo.socials.findGitHub()?.url,
                linkedinUrl = event.eventInfo.socials.findLinkedIn()?.url,
                websiteUrl = event.eventInfo.socials.findWebsite()?.url,
                emailUrl = event.eventInfo.socials.findEmail()?.url
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
