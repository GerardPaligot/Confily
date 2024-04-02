package org.gdglille.devfest.android.theme.m3.schedules.ui.filters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
import org.gdglille.devfest.android.shared.resources.Resource
import org.gdglille.devfest.android.shared.resources.title_filters_categories
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.android.theme.m3.style.chips.FilterChip
import org.gdglille.devfest.android.theme.m3.style.schedules.filters.FilterSection
import org.gdglille.devfest.android.theme.m3.style.schedules.findCategoryImageVector
import org.gdglille.devfest.models.ui.CategoryUi
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalLayoutApi::class, ExperimentalResourceApi::class)
@Composable
fun CategoryListFilters(
    categories: ImmutableMap<CategoryUi, Boolean>,
    modifier: Modifier = Modifier,
    onClick: (categoryUi: CategoryUi, selected: Boolean) -> Unit
) {
    FilterSection(
        title = stringResource(Resource.string.title_filters_categories),
        modifier = modifier
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            categories.entries.forEach { entry ->
                FilterChip(
                    label = entry.key.name,
                    selected = entry.value,
                    imageVector = entry.key.icon?.findCategoryImageVector(),
                    onClick = { onClick(entry.key, it) }
                )
            }
        }
    }
}

@Preview
@Composable
private fun CategoryListPreview() {
    Conferences4HallTheme {
        CategoryListFilters(
            categories = persistentMapOf(
                CategoryUi.fake to true,
                CategoryUi.fake to true,
                CategoryUi.fake to false,
                CategoryUi.fake to false,
                CategoryUi.fake to false,
                CategoryUi.fake to false,
                CategoryUi.fake to false,
                CategoryUi.fake to false,
                CategoryUi.fake to false,
                CategoryUi.fake to false,
                CategoryUi.fake to false,
                CategoryUi.fake to false
            ),
            onClick = { _, _ -> }
        )
    }
}
