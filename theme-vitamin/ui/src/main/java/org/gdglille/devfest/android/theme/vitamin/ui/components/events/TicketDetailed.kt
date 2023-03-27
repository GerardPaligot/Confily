package org.gdglille.devfest.android.theme.vitamin.ui.components.events

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decathlon.vitamin.compose.foundation.VitaminTheme
import org.gdglille.devfest.Image
import org.gdglille.devfest.android.theme.vitamin.ui.theme.Conferences4HallTheme
import org.gdglille.devfest.android.theme.vitamin.ui.theme.placeholder
import org.gdglille.devfest.android.ui.resources.DottedShape
import org.gdglille.devfest.android.ui.resources.R
import org.gdglille.devfest.models.TicketInfoUi
import org.gdglille.devfest.models.TicketUi

private const val TicketRatio = 2 / 3

@Composable
fun TicketDetailed(
    ticket: TicketInfoUi,
    qrCode: Image?,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    shape: Shape = RoundedCornerShape(4.dp),
) {
    val cdTicketId = stringResource(R.string.semantic_ticket_id, ticket.id)
    Card(
        modifier = modifier.wrapContentHeight(),
        shape = shape,
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
                        text = stringResource(R.string.text_ticket_firstname),
                        style = VitaminTheme.typography.body3,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Start
                    )
                    Text(
                        text = ticket.firstName,
                        style = VitaminTheme.typography.body3,
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
                        text = stringResource(R.string.text_ticket_lastname),
                        style = VitaminTheme.typography.body3,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Start
                    )
                    Text(
                        text = ticket.lastName,
                        style = VitaminTheme.typography.body3,
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
                    style = VitaminTheme.typography.body3,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .semantics { text = AnnotatedString(cdTicketId) }
                        .placeholder(isLoading)
                )
                if (qrCode != null) {
                    BoxWithConstraints {
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
        }
    }
}

@Preview
@Composable
fun TicketPreview() {
    Conferences4HallTheme {
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
fun TicketPreviewLongText() {
    Conferences4HallTheme {
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
