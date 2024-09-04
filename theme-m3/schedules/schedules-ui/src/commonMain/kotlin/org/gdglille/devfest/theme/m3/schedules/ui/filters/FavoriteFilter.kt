package org.gdglille.devfest.theme.m3.schedules.ui.filters

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
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.action_filtering_favorites
import com.paligot.confily.resources.title_filters_favorites
import com.paligot.confily.style.schedules.filters.FilterSection
import org.jetbrains.compose.resources.stringResource

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
