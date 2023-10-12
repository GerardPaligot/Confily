package org.gdglille.devfest.android.theme.m3.schedules.ui.filters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
import org.gdglille.devfest.android.theme.m3.style.chips.FilterChip
import org.gdglille.devfest.android.theme.m3.schedules.ui.talks.ShortTalk
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.android.theme.m3.style.R
import org.gdglille.devfest.models.ui.FormatUi

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FormatListFilters(
    formats: ImmutableMap<FormatUi, Boolean>,
    modifier: Modifier = Modifier,
    onClick: (formatUi: FormatUi, selected: Boolean) -> Unit
) {
    FilterSection(
        title = stringResource(id = R.string.title_filters_formats),
        modifier = modifier
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            formats.entries.forEach { entry ->
                FilterChip(
                    label = entry.key.name,
                    selected = entry.value,
                    imageVector = if (entry.key.time <= ShortTalk) Icons.Outlined.Bolt else Icons.Outlined.Timer,
                    onClick = { onClick(entry.key, it) }
                )
            }
        }
    }
}

@Preview
@Composable
private fun FormatListPreview() {
    Conferences4HallTheme {
        FormatListFilters(
            formats = persistentMapOf(
                FormatUi.quickie to true,
                FormatUi.conference to false
            ),
            onClick = { _, _ -> }
        )
    }
}
