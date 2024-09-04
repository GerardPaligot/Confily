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
import com.paligot.confily.models.ui.CategoryUi
import com.paligot.confily.models.ui.FiltersUi
import com.paligot.confily.models.ui.FormatUi
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.screen_agenda_filters
import com.paligot.confily.style.theme.Scaffold
import com.paligot.confily.style.theme.SpacingTokens
import com.paligot.confily.style.theme.appbars.AppBarIcons
import com.paligot.confily.style.theme.toDp
import org.gdglille.devfest.theme.m3.schedules.ui.filters.CategoryListFilters
import org.gdglille.devfest.theme.m3.schedules.ui.filters.FavoriteFilter
import org.gdglille.devfest.theme.m3.schedules.ui.filters.FormatListFilters
import org.jetbrains.compose.resources.stringResource

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
