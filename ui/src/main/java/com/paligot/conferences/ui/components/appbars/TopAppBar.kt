package com.paligot.conferences.ui.components.appbars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.paligot.conferences.ui.theme.Conferences4HallTheme

@Composable
fun TopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (AppBarIcons.() -> Unit)? = null,
    actions: List<ActionItem> = emptyList(),
    onActionClicked: ((ActionItemId) -> Unit)? = null
) {
    androidx.compose.material.TopAppBar(
        title = { Text(text = title) },
        modifier = modifier,
        navigationIcon = navigationIcon.takeOrNull(),
        actions = {
            actions.forEach { action ->
                IconButton(onClick = { onActionClicked?.let { it(action.id) } }) {
                    Icon(
                        imageVector = action.icon,
                        contentDescription = action.contentDescription
                    )
                }
            }
        }
    )
}

open class ActionItem(
    val icon: ImageVector,
    val contentDescription: String?,
    val id: ActionItemId,
)

sealed class ActionItemId {
    object QrCodeActionItem: ActionItemId()
    object ShareActionItem: ActionItemId()
    object ReportActionItem: ActionItemId()
}

internal fun (@Composable AppBarIcons.() -> Unit)?.takeOrNull(): (@Composable () -> Unit)? {
    if (this == null) return null
    return {
        AppBarIcons.this()
    }
}

@Preview
@Composable
fun TopAppBarPeview() {
    Conferences4HallTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            TopAppBar(
                title = "Speakers"
            )
            TopAppBar(
                title = "Speakers",
                navigationIcon = { Back { } }
            )
            TopAppBar(
                title = "QrCode Scanner",
                navigationIcon = { Back { } },
                actions = arrayListOf(
                    ActionItem(
                        icon = Icons.Filled.QrCodeScanner,
                        contentDescription = "Scan QrCode",
                        id = ActionItemId.QrCodeActionItem
                    )
                )
            )
        }
    }
}
