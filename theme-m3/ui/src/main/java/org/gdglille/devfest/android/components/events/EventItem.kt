package org.gdglille.devfest.android.components.events

import androidx.compose.foundation.clickable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.gdglille.devfest.android.theme.Conferences4HallTheme
import org.gdglille.devfest.android.theme.placeholder
import org.gdglille.devfest.models.EventItemUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventItem(
    item: EventItemUi,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    onClick: (String) -> Unit
) {
    ListItem(
        headlineText = {
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
fun EventItemPreview() {
    Conferences4HallTheme {
        EventItem(
            item = EventItemUi.fake,
            onClick = {}
        )
    }
}
