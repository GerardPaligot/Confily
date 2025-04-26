package com.paligot.confily.schedules.panes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.screen_agenda_filters
import com.paligot.confily.schedules.ui.filters.CategoryListFilters
import com.paligot.confily.schedules.ui.filters.FavoriteFilter
import com.paligot.confily.schedules.ui.filters.FormatListFilters
import com.paligot.confily.schedules.ui.models.FiltersUi
import com.paligot.confily.style.theme.ConfilyTheme
import com.paligot.confily.style.theme.Scaffold
import com.paligot.confily.style.theme.SpacingTokens
import com.paligot.confily.style.theme.appbars.AppBarIcons
import com.paligot.confily.style.theme.toDp
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AgendaFiltersPane(
    filtersUi: FiltersUi,
    onFavoriteClick: (selected: Boolean) -> Unit,
    onCategoryClick: (categoryId: String, selected: Boolean) -> Unit,
    onFormatClick: (formatId: String, selected: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.background,
    navigationIcon: @Composable (AppBarIcons.() -> Unit)? = null
) {
    Scaffold(
        title = stringResource(Resource.string.screen_agenda_filters),
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
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.LargeSpacing.toDp())
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
private fun AgendaFiltersPanePreview() {
    ConfilyTheme {
        AgendaFiltersPane(
            filtersUi = FiltersUi.fake,
            onFavoriteClick = {},
            onCategoryClick = { _, _ -> },
            onFormatClick = { _, _ -> }
        )
    }
}
