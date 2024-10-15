package com.paligot.confily.infos.ui.tickets

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.models.ui.TicketUi
import com.paligot.confily.style.theme.ConfilyTheme

@Preview
@Composable
private fun TicketPreview() {
    ConfilyTheme {
        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            TicketDetailed(
                ticket = TicketUi.fake.info!!,
                qrCode = TicketUi.fake.qrCode,
                modifier = Modifier.width(maxWidth)
            )
        }
    }
}

@Preview
@Composable
private fun TicketPreviewLongText() {
    ConfilyTheme {
        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            TicketDetailed(
                ticket = TicketUi.fake.info!!,
                qrCode = TicketUi.fake.qrCode,
                modifier = Modifier.width(maxWidth)
            )
        }
    }
}
