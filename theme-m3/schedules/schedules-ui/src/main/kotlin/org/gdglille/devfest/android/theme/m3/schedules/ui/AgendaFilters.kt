package org.gdglille.devfest.android.theme.m3.schedules.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.theme.m3.schedules.ui.filters.CategoryListFilters
import org.gdglille.devfest.android.theme.m3.schedules.ui.filters.FavoriteFilter
import org.gdglille.devfest.android.theme.m3.schedules.ui.filters.FormatListFilters
import org.gdglille.devfest.android.theme.m3.style.Scaffold
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.android.ui.resources.R
import org.gdglille.devfest.models.CategoryUi
import org.gdglille.devfest.models.FiltersUi
import org.gdglille.devfest.models.FormatUi

@Composable
fun AgendaFilters(
    filtersUi: FiltersUi,
    onFavoriteClick: (selected: Boolean) -> Unit,
    onCategoryClick: (categoryUi: CategoryUi, selected: Boolean) -> Unit,
    onFormatClick: (formatUi: FormatUi, selected: Boolean) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        title = R.string.screen_agenda_filters,
        navigationIcon = { Back(onClick = onBack) },
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it),
            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                FavoriteFilter(isFavorite = filtersUi.onlyFavorites, onClick = onFavoriteClick)
            }
            item {
                CategoryListFilters(categories = filtersUi.categories, onClick = onCategoryClick)
            }
            item {
                FormatListFilters(formats = filtersUi.formats, onClick = onFormatClick)
            }
        }
    }
}

@Preview
@Composable
private fun AgendaFiltersPreview() {
    Conferences4HallTheme {
        AgendaFilters(
            filtersUi = FiltersUi.fake,
            onFavoriteClick = {},
            onCategoryClick = { _, _ -> },
            onFormatClick = { _, _ -> },
            onBack = {}
        )
    }
}
