package com.paligot.confily.schedules.ui.filters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.paligot.confily.models.ui.CategoryUi
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.title_filters_categories
import com.paligot.confily.style.schedules.filters.FilterSection
import com.paligot.confily.style.schedules.findCategoryImageVector
import com.paligot.confily.style.theme.chips.FilterChip
import kotlinx.collections.immutable.ImmutableMap
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalLayoutApi::class)
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
