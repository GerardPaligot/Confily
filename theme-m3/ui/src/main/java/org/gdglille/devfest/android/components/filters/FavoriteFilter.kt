package org.gdglille.devfest.android.components.filters

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.android.ui.resources.R

@Composable
fun FavoriteFilter(
    isFavorite: Boolean,
    onClick: (selected: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    FilterSection(
        title = stringResource(id = R.string.title_filters_favorites),
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
            Text(text = stringResource(id = R.string.action_filtering_favorites))
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
