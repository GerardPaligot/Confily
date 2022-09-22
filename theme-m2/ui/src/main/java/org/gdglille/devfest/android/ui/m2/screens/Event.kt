package org.gdglille.devfest.android.ui.m2.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.ui.m2.components.events.EventSection
import org.gdglille.devfest.android.ui.m2.components.tickets.TicketDetailed
import org.gdglille.devfest.android.ui.m2.components.tickets.TicketQrCode
import org.gdglille.devfest.models.EventUi

@Composable
fun Event(
    event: EventUi,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    onLinkClicked: (url: String) -> Unit,
    onTicketScannerClicked: () -> Unit,
    onMenusClicked: () -> Unit
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 8.dp),
        contentPadding = PaddingValues(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            EventSection(
                eventInfo = event.eventInfo,
                isLoading = isLoading,
                onFaqClick = onLinkClicked,
                onCoCClick = onLinkClicked,
                onTicketScannerClicked = onTicketScannerClicked,
                onMenusClicked = onMenusClicked,
                onTwitterClick = onLinkClicked,
                onLinkedInClick = onLinkClicked
            )
        }
        event.ticket?.let {
            item {
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
    }
}
