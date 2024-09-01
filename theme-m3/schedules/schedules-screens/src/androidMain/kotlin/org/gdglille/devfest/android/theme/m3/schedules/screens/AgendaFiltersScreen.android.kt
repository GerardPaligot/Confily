package org.gdglille.devfest.android.theme.m3.schedules.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.models.ui.FiltersUi

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
