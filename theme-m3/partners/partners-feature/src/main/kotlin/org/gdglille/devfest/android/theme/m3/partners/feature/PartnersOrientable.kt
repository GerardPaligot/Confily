package org.gdglille.devfest.android.theme.m3.partners.feature

import android.content.res.Configuration
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import org.gdglille.devfest.models.ui.PartnerGroupsUi

@Composable
fun PartnersOrientable(
    partners: PartnerGroupsUi,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    onPartnerClick: (id: String) -> Unit
) {
    val orientation = LocalConfiguration.current
    val state = rememberLazyGridState()
    if (orientation.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        Partners(
            partners = partners,
            modifier = modifier,
            columnCount = 6,
            state = state,
            isLoading = isLoading,
            onPartnerClick = onPartnerClick
        )
    } else {
        Partners(
            partners = partners,
            modifier = modifier,
            columnCount = 3,
            state = state,
            isLoading = isLoading,
            onPartnerClick = onPartnerClick
        )
    }
}
