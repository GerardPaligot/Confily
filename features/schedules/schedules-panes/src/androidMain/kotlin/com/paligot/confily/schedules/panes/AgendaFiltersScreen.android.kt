package com.paligot.confily.schedules.panes

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.schedules.ui.models.FiltersUi
import com.paligot.confily.style.theme.ConfilyTheme

@Preview
@Composable
private fun AgendaFiltersPreview() {
    ConfilyTheme {
        AgendaFiltersScreen(
            filtersUi = FiltersUi.fake,
            onFavoriteClick = {},
            onCategoryClick = { _, _ -> },
            onFormatClick = { _, _ -> }
        )
    }
}
