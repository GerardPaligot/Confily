package org.gdglille.devfest.android.theme.m3.schedules.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.gdglille.devfest.android.theme.m3.schedules.ui.filters.CategoryListFilters
import org.gdglille.devfest.android.theme.m3.schedules.ui.filters.FavoriteFilter
import org.gdglille.devfest.android.theme.m3.schedules.ui.filters.FormatListFilters
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.android.theme.m3.style.R
import org.gdglille.devfest.android.theme.m3.style.Scaffold
import org.gdglille.devfest.android.theme.m3.style.SpacingTokens
import org.gdglille.devfest.android.theme.m3.style.appbars.AppBarIcons
import org.gdglille.devfest.android.theme.m3.style.toDp
import org.gdglille.devfest.models.ui.CategoryUi
import org.gdglille.devfest.models.ui.FiltersUi
import org.gdglille.devfest.models.ui.FormatUi

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AgendaFiltersScreen(
    filtersUi: FiltersUi,
    onFavoriteClick: (selected: Boolean) -> Unit,
    onCategoryClick: (categoryUi: CategoryUi, selected: Boolean) -> Unit,
    onFormatClick: (formatUi: FormatUi, selected: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.background,
    navigationIcon: @Composable (AppBarIcons.() -> Unit)? = null
) {
    Scaffold(
        title = stringResource(id = R.string.screen_agenda_filters),
        navigationIcon = navigationIcon,
        containerColor = containerColor,
        hasScrollBehavior = false,
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = it.calculateTopPadding()),
            contentPadding = PaddingValues(
                vertical = SpacingTokens.MediumSpacing.toDp(),
                horizontal = SpacingTokens.LargeSpacing.toDp()
            ),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.LargeSpacing.toDp()),
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
        AgendaFiltersScreen(
            filtersUi = FiltersUi.fake,
            onFavoriteClick = {},
            onCategoryClick = { _, _ -> },
            onFormatClick = { _, _ -> }
        )
    }
}
