package org.gdglille.devfest.android.theme.vitamin.ui.components.events

import androidx.compose.foundation.clickable
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.gdglille.devfest.android.theme.vitamin.ui.theme.Conferences4HallTheme
import org.gdglille.devfest.android.theme.vitamin.ui.theme.placeholder
import org.gdglille.devfest.models.EventItemUi

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EventItem(
    item: EventItemUi,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    onClick: (String) -> Unit
) {
    ListItem(
        text = {
            Text(
                text = item.name,
                modifier = Modifier.placeholder(visible = isLoading)
            )
        },
        trailing = {
            Text(
                text = item.date,
                modifier = Modifier.placeholder(visible = isLoading)
            )
        },
        modifier = modifier.clickable {
            onClick(item.id)
        }
    )
}

@Preview
@Composable
fun EventItemPreview() {
    Conferences4HallTheme {
        EventItem(
            item = EventItemUi.fake,
            onClick = {}
        )
    }
}
