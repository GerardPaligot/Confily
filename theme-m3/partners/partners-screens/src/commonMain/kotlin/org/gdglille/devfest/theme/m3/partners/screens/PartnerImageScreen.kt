package org.gdglille.devfest.theme.m3.partners.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.paligot.confily.style.partners.items.PartnerItem
import org.gdglille.devfest.theme.m3.style.placeholder.placeholder

@Composable
fun PartnerImageScreen(
    url: String,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        PartnerItem(
            url = url,
            contentDescription = null,
            modifier = Modifier
                .width(128.dp)
                .aspectRatio(ratio = 1f)
                .placeholder(visible = isLoading),
            onClick = {}
        )
    }
}
