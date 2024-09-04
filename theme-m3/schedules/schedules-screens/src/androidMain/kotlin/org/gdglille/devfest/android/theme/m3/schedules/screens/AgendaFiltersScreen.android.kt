package org.gdglille.devfest.android.theme.m3.schedules.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.models.ui.FiltersUi
import com.paligot.confily.style.theme.Conferences4HallTheme

@Preview
@Composable
private fun AgendaFiltersPreview() {
    Conferences4HallTheme {
        AgendaFiltersScreen(
            filtersUi = FiltersUi.fake,
            onFavoriteClick = {},
            onCategoryClick = { _, _ -> },
            onFormatClick = { _, _ -> }
        )
    }
}
