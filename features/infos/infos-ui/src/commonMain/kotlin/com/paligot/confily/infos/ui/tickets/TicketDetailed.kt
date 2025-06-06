package com.paligot.confily.infos.ui.tickets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.paligot.confily.infos.ui.models.TicketInfoUi
import com.paligot.confily.infos.ui.models.TicketUi
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.semantic_ticket_id
import com.paligot.confily.resources.semantic_ticket_qrcode
import com.paligot.confily.resources.text_ticket_firstname
import com.paligot.confily.resources.text_ticket_lastname
import com.paligot.confily.style.components.placeholder.placeholder
import com.paligot.confily.style.theme.ConfilyTheme
import com.paligot.confily.style.theme.shapes.DottedShape
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.decodeToImageBitmap
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalResourceApi::class)
@Composable
fun TicketDetailed(
    ticket: TicketInfoUi,
    qrCode: ByteArray?,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    shape: Shape = RoundedCornerShape(16.dp),
    elevation: Dp = 8.dp
) {
    val qrCodeBitmap = remember(qrCode) { qrCode?.decodeToImageBitmap() }
    val cdTicketId = stringResource(Resource.string.semantic_ticket_id, ticket.id)
    Surface(
        modifier = modifier.wrapContentHeight(),
        shape = shape,
        tonalElevation = elevation
    ) {
        Column {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(vertical = 24.dp, horizontal = 28.dp)
            ) {
                Row(
                    modifier = Modifier
                        .semantics(mergeDescendants = true) {}
                        .placeholder(isLoading)
                ) {
                    Text(
                        text = stringResource(Resource.string.text_ticket_firstname),
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Start
                    )
                    Text(
                        text = ticket.firstName,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.End,
                        fontWeight = FontWeight.Bold
                    )
                }
                Row(
                    modifier = Modifier
                        .semantics(mergeDescendants = true) {}
                        .placeholder(isLoading)
                ) {
                    Text(
                        text = stringResource(Resource.string.text_ticket_lastname),
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Start
                    )
                    Text(
                        text = ticket.lastName,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.End,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Box(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(color = LocalContentColor.current, shape = DottedShape(10.dp))
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .padding(vertical = 24.dp, horizontal = 28.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = ticket.id,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .semantics { text = AnnotatedString(cdTicketId) }
                        .placeholder(isLoading)
                )
                if (qrCodeBitmap != null) {
                    Image(
                        bitmap = qrCodeBitmap,
                        contentDescription = stringResource(
                            Resource.string.semantic_ticket_qrcode
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .placeholder(isLoading)
                    )
                }
            }
        }
    }
}

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
