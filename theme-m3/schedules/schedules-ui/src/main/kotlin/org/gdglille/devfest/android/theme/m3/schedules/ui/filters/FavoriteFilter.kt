package org.gdglille.devfest.android.theme.m3.schedules.ui.filters

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.android.shared.resources.Resource
import org.gdglille.devfest.android.shared.resources.action_filtering_favorites
import org.gdglille.devfest.android.theme.m3.style.schedules.filters.FilterSection
import org.gdglille.devfest.android.shared.resources.title_filters_favorites
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun FavoriteFilter(
    isFavorite: Boolean,
    onClick: (selected: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    FilterSection(
        title = stringResource(Resource.string.title_filters_favorites),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(role = Role.Checkbox) { onClick(isFavorite.not()) }
        ) {
            Checkbox(
                checked = isFavorite,
                onCheckedChange = null,
                modifier = Modifier.minimumInteractiveComponentSize()
            )
            Text(text = stringResource(Resource.string.action_filtering_favorites))
        }
    }
}

@Preview
@Composable
private fun FavoriteFilterPreview() {
    Conferences4HallTheme {
        FavoriteFilter(
            isFavorite = true,
            onClick = {}
        )
    }
}
