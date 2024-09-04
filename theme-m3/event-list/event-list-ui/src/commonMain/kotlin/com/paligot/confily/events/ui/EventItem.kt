package com.paligot.confily.events.ui

import androidx.compose.foundation.clickable
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.paligot.confily.style.components.placeholder.placeholder

@Composable
fun EventItem(
    item: com.paligot.confily.models.ui.EventItemUi,
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
