package org.gdglille.devfest.theme.m3.schedules.ui.filters

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.models.ui.CategoryUi
import kotlinx.collections.immutable.persistentMapOf
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme

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
