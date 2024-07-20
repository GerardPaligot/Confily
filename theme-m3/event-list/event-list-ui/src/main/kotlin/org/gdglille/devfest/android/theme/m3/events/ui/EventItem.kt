package org.gdglille.devfest.android.theme.m3.events.ui

import androidx.compose.foundation.clickable
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.models.ui.EventItemUi
import org.gdglille.devfest.theme.m3.style.placeholder.placeholder

@Composable
fun EventItem(
    item: EventItemUi,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    onClick: (String) -> Unit
) {
    ListItem(
        headlineContent = {
            Text(
                text = item.name,
                modifier = Modifier.placeholder(visible = isLoading)
            )
        },
        trailingContent = {
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
private fun EventItemPreview() {
    Conferences4HallTheme {
        EventItem(
            item = EventItemUi.fake,
            onClick = {}
        )
    }
}
