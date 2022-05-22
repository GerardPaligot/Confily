package org.gdglille.devfest.android.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.components.events.EventSection
import org.gdglille.devfest.android.components.tickets.TicketDetailed
import org.gdglille.devfest.android.components.tickets.TicketQrCode
import org.gdglille.devfest.models.EventUi

@Composable
fun Event(
    logo: Int,
    event: EventUi,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    onFaqClick: (url: String) -> Unit,
    onCoCClick: (url: String) -> Unit,
    onTicketScannerClicked: () -> Unit,
    onTwitterClick: (url: String?) -> Unit,
    onLinkedInClick: (url: String?) -> Unit
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
                onLinkedInClick = onLinkedInClick,
                onTicketScannerClicked = onTicketScannerClicked
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
