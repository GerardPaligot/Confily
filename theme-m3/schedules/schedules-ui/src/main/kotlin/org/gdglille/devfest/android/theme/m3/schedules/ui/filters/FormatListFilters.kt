package org.gdglille.devfest.android.theme.m3.schedules.ui.filters

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
import org.gdglille.devfest.android.theme.m3.style.chips.FilterChip
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.android.shared.resources.Resource
import org.gdglille.devfest.android.theme.m3.style.schedules.filters.FilterSection
import org.gdglille.devfest.android.theme.m3.style.schedules.findTimeImageVector
import org.gdglille.devfest.android.shared.resources.title_filters_formats
import org.gdglille.devfest.models.ui.FormatUi
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalLayoutApi::class, ExperimentalResourceApi::class)
@Composable
fun FormatListFilters(
    formats: ImmutableMap<FormatUi, Boolean>,
    modifier: Modifier = Modifier,
    onClick: (formatUi: FormatUi, selected: Boolean) -> Unit
) {
    FilterSection(
        title = stringResource(Resource.string.title_filters_formats),
        modifier = modifier
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            formats.entries.forEach { entry ->
                FilterChip(
                    label = entry.key.name,
                    selected = entry.value,
                    imageVector = entry.key.time.findTimeImageVector(),
                    onClick = { onClick(entry.key, it) }
                )
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FormatListPreview() {
    Conferences4HallTheme {
        Surface {
            FormatListFilters(
                formats = persistentMapOf(
                    FormatUi.quickie to true,
                    FormatUi.conference to false
                ),
                onClick = { _, _ -> }
            )
        }
    }
}
