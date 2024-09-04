package com.paligot.confily.schedules.ui.filters

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.models.ui.CategoryUi
import com.paligot.confily.style.theme.Conferences4HallTheme
import kotlinx.collections.immutable.persistentMapOf

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
