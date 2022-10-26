package org.gdglille.devfest.android.components.appbars

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.theme.Conferences4HallTheme

@Composable
fun Tab(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selectedBackgroundColor: Color = MaterialTheme.colorScheme.primary,
    unselectedBackgroundColor: Color = MaterialTheme.colorScheme.surface,
    selectedContentColor: Color = MaterialTheme.colorScheme.contentColorFor(selectedBackgroundColor),
    unselectedContentColor: Color = LocalContentColor.current,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    val ripple = rememberRipple(bounded = true, color = selectedBackgroundColor)
    val backgroundColor = if (selected) selectedBackgroundColor else unselectedBackgroundColor
    val contentColor = if (selected) selectedContentColor else unselectedContentColor
    Box(
        modifier = modifier.height(ContainerHeight),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            modifier = Modifier
                .background(color = backgroundColor, shape = MaterialTheme.shapes.medium)
                .clip(MaterialTheme.shapes.medium)
                .selectable(
                    selected = selected,
                    onClick = onClick,
                    enabled = enabled,
                    role = Role.Tab,
                    interactionSource = interactionSource,
                    indication = ripple
                )
                .padding(horizontal = 8.dp, vertical = 4.dp),
            color = contentColor
        )
    }
}

private val ContainerHeight = 48.0.dp

@Preview
@Composable
fun TabPreview() {
    Conferences4HallTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Tab(
                text = "10 June",
                selected = true,
                onClick = {}
            )
            Tab(
                text = "11 June",
                selected = false,
                onClick = {}
            )
        }
    }
}
