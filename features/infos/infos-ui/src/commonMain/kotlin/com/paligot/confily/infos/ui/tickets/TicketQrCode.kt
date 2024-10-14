package com.paligot.confily.infos.ui.tickets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.semantic_ticket_qrcode
import com.paligot.confily.style.components.placeholder.placeholder
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.decodeToImageBitmap
import org.jetbrains.compose.resources.stringResource

private const val TicketRatio = 3 / 4

@OptIn(ExperimentalResourceApi::class)
@Composable
fun TicketQrCode(
    qrCode: ByteArray,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    shape: Shape = RoundedCornerShape(16.dp),
    elevation: Dp = 8.dp
) {
    val qrCodeBitmap = remember(qrCode) { qrCode.decodeToImageBitmap() }
    Surface(
        modifier = modifier.wrapContentHeight(),
        shape = shape,
        tonalElevation = elevation
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .padding(vertical = 24.dp, horizontal = 28.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                bitmap = qrCodeBitmap,
                contentDescription = stringResource(Resource.string.semantic_ticket_qrcode),
                modifier = Modifier
                    .size(this.maxWidth * TicketRatio)
                    .placeholder(isLoading)
            )
        }
    }
}
