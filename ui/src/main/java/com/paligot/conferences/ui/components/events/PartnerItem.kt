package com.paligot.conferences.ui.components.events

import android.graphics.drawable.ColorDrawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.Indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberImagePainter
import coil.decode.SvgDecoder
import com.paligot.conferences.repositories.PartnerItemUi
import com.paligot.conferences.ui.theme.Conferences4HallTheme
import com.paligot.conferences.ui.theme.placeholder
import android.graphics.Color as AndroidColor

val partnerUi = PartnerItemUi(
    logoUrl = "https://pbs.twimg.com/profile_images/1483539472574816261/mi3QaL7u_400x400.png",
    siteUrl = "https://devfest.gdglille.org/",
    name = "Devfest"
)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PartnerItem(
    partnerUi: PartnerItemUi,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    backgroundColor: Color = MaterialTheme.colors.surface,
    shape: Shape = MaterialTheme.shapes.medium,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    indication: Indication = rememberRipple(),
    contentScale: ContentScale = ContentScale.Fit,
    onClick: (siteUrl: String?) -> Unit
) {
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .componentRegistry {
            add(SvgDecoder(LocalContext.current))
        }
        .build()
    Surface(
        modifier = modifier.placeholder(visible = isLoading),
        role = Role.Button,
        indication = indication,
        interactionSource = interactionSource,
        color = backgroundColor,
        shape = shape,
        onClick = { onClick(partnerUi.siteUrl) },
    ) {
        Image(
            painter = rememberImagePainter(partnerUi.logoUrl,
                builder = {
                    val r = MaterialTheme.colors.primary.red
                    val g = MaterialTheme.colors.primary.green
                    val b = MaterialTheme.colors.primary.blue
                    val color = AndroidColor.rgb((r * 100).toInt(), (g * 100).toInt(), (b * 100).toInt())
                    this.placeholder(ColorDrawable(color))
                },
                imageLoader = imageLoader
            ),
            contentDescription = partnerUi.name,
            modifier = modifier,
            contentScale = contentScale
        )
    }
}

@Preview
@Composable
fun PartnerItemPreview() {
    Conferences4HallTheme {
        PartnerItem(
            partnerUi = partnerUi,
            modifier = Modifier.size(200.dp),
            onClick = {}
        )
    }
}
