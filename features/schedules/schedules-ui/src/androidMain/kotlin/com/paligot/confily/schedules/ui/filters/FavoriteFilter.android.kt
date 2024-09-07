package com.paligot.confily.schedules.ui.filters

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.style.theme.ConfilyTheme

@Preview
@Composable
private fun FavoriteFilterPreview() {
    ConfilyTheme {
        FavoriteFilter(
            isFavorite = true,
            onClick = {}
        )
    }
}
