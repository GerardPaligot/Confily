package com.paligot.confily.events.ui

import androidx.compose.foundation.clickable
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.clearAndSetSemantics
import com.paligot.confily.events.ui.models.EventItemUi
import com.paligot.confily.style.components.placeholder.placeholder

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
        modifier = modifier
            .clearSemantics(apply = isLoading) {}
            .clickable(enabled = isLoading.not()) {
                onClick(item.id)
            }
    )
}

fun Modifier.clearSemantics(
    apply: Boolean,
    properties: (SemanticsPropertyReceiver.() -> Unit)
) = composed {
    if (apply) {
        this.then(Modifier.clearAndSetSemantics(properties))
    } else {
        this
    }
}
