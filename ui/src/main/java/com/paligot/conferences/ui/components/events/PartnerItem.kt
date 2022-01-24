package com.paligot.conferences.ui.components.events

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.paligot.conferences.repositories.PartnerItemUi
import com.paligot.conferences.ui.theme.Conferences4HallTheme

val partnerUi = PartnerItemUi(
    logoUrl = "https://pbs.twimg.com/profile_images/1483539472574816261/mi3QaL7u_400x400.png",
    siteUrl = "https://devfest.gdglille.org/",
    name = "Devfest"
)

@Composable
fun PartnerItem(
    partnerUi: PartnerItemUi,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    indication: Indication = rememberRipple(),
    contentScale: ContentScale = ContentScale.FillHeight,
    onClick: (siteUrl: String?) -> Unit
) {
    Image(
        painter = rememberImagePainter(partnerUi.logoUrl, builder = {
            val r = MaterialTheme.colors.primary.red
            val g = MaterialTheme.colors.primary.green
            val b = MaterialTheme.colors.primary.blue
            val color = Color.rgb((r * 100).toInt(), (g * 100).toInt(), (b * 100).toInt())
            this.placeholder(ColorDrawable(color))
        }),
        contentDescription = partnerUi.name,
        modifier = modifier.clickable(
            interactionSource = interactionSource,
            indication = indication,
            onClick = { onClick(partnerUi.siteUrl) },
            role = Role.Button
        ),
        contentScale = contentScale
    )
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
