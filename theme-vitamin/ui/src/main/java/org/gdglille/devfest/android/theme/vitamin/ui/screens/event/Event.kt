package org.gdglille.devfest.android.theme.vitamin.ui.screens.event

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decathlon.vitamin.compose.foundation.VitaminTheme
import org.gdglille.devfest.android.theme.vitamin.ui.components.events.EventMap
import org.gdglille.devfest.android.theme.vitamin.ui.components.events.TicketDetailed
import org.gdglille.devfest.android.theme.vitamin.ui.components.events.TicketQrCode
import org.gdglille.devfest.android.theme.vitamin.ui.components.structure.SocialsSection
import org.gdglille.devfest.android.theme.vitamin.ui.theme.Conferences4HallTheme
import org.gdglille.devfest.android.ui.resources.R
import org.gdglille.devfest.models.EventUi

@Composable
fun Event(
    event: EventUi,
    onLinkClicked: (url: String?) -> Unit,
    onItineraryClicked: (lat: Double, lng: Double) -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
) {
    LazyColumn(modifier = modifier) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(VitaminTheme.colors.vtmnBackgroundPrimary)
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                SocialsSection(
                    title = event.eventInfo.name,
                    subtitle = event.eventInfo.date,
                    isLoading = isLoading,
                    twitterUrl = event.eventInfo.twitterUrl,
                    linkedinUrl = event.eventInfo.linkedinUrl,
                    onLinkClicked = onLinkClicked,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
        event.ticket?.let {
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
            item {
                Text(
                    text = stringResource(R.string.title_ticket),
                    style = VitaminTheme.typography.h6,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (it.info != null) {
                    TicketDetailed(
                        ticket = it.info!!,
                        qrCode = it.qrCode,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        isLoading = isLoading
                    )
                } else if (it.qrCode != null) {
                    TicketQrCode(
                        qrCode = it.qrCode!!,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        isLoading = isLoading
                    )
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
        item {
            Text(
                text = stringResource(R.string.title_plan),
                style = VitaminTheme.typography.h6,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            EventMap(
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
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Preview
@Composable
fun EventPreview() {
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
