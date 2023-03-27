package org.gdglille.devfest.android.theme.vitamin.ui.components.events

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.Image
import org.gdglille.devfest.android.theme.vitamin.ui.theme.placeholder
import org.gdglille.devfest.android.ui.resources.R

private const val TicketRatio = 3 / 4

@Composable
fun TicketQrCode(
    qrCode: Image,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    shape: Shape = RoundedCornerShape(4.dp)
) {
    Card(
        modifier = modifier.wrapContentHeight(),
        shape = shape,
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .padding(vertical = 24.dp, horizontal = 28.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                bitmap = qrCode.asImageBitmap(),
                contentDescription = stringResource(id = R.string.semantic_ticket_qrcode),
                modifier = Modifier
                    .size(this.maxWidth * TicketRatio)
                    .placeholder(isLoading)
            )
        }
    }
}
