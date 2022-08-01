package org.gdglille.devfest.android.theme.vitamin.ui.components.partners

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.imageLoader
import com.decathlon.vitamin.compose.foundation.VitaminTheme
import org.gdglille.devfest.android.theme.vitamin.ui.theme.Conferences4HallTheme
import org.gdglille.devfest.android.theme.vitamin.ui.theme.placeholder
import org.gdglille.devfest.models.PartnerItemUi

@ExperimentalMaterialApi
@Composable
fun PartnerItem(
    partnerUi: PartnerItemUi,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    backgroundColor: Color = VitaminTheme.colors.vtmnBackgroundTertiary,
    shape: Shape = MaterialTheme.shapes.medium,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    contentScale: ContentScale = ContentScale.Fit,
    onClick: (siteUrl: String?) -> Unit
) {
    Surface(
        onClick = { onClick(partnerUi.siteUrl) },
        modifier = modifier.placeholder(visible = isLoading),
        shape = shape,
        color = backgroundColor,
        interactionSource = interactionSource
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = partnerUi.logoUrl,
                placeholder = ColorPainter(backgroundColor),
                imageLoader = LocalContext.current.imageLoader
            ),
            contentDescription = partnerUi.name,
            contentScale = contentScale,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
fun PartnerItemPreview() {
    Conferences4HallTheme {
        PartnerItem(
            partnerUi = PartnerItemUi.fake,
            modifier = Modifier.size(200.dp),
            onClick = {}
        )
    }
}
