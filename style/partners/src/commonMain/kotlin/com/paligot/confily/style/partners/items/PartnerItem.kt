package com.paligot.confily.style.partners.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import coil3.compose.rememberAsyncImagePainter
import com.paligot.confily.style.theme.toDp

@Composable
fun PartnerItem(
    url: String,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = PartnerItemDefaults.containerColor,
    placeholder: Painter = PartnerItemDefaults.placeholderPainter,
    shape: Shape = PartnerItemDefaults.shape,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    contentScale: ContentScale = ContentScale.Fit
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        color = backgroundColor,
        interactionSource = interactionSource
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = url,
                placeholder = placeholder
            ),
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = Modifier.padding(PartnerItemTokens.PaddingSpacing.toDp())
        )
    }
}
