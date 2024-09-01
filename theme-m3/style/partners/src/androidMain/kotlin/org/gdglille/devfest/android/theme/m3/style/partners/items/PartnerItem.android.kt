package org.gdglille.devfest.android.theme.m3.style.partners.items

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.theme.m3.style.partners.items.PartnerItem

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
