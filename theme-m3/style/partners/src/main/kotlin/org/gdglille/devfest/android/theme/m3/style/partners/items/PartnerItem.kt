package org.gdglille.devfest.android.theme.m3.style.partners.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberAsyncImagePainter
import coil.imageLoader
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.android.theme.m3.style.toDp

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
                placeholder = placeholder,
                imageLoader = LocalContext.current.imageLoader
            ),
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = Modifier.padding(PartnerItemTokens.PaddingSpacing.toDp())
        )
    }
}

@Preview
@Composable
private fun PartnerItemPreview() {
    Conferences4HallTheme {
        PartnerItem(
            url = "",
            contentDescription = "Gerard Inc.",
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        )
    }
}
