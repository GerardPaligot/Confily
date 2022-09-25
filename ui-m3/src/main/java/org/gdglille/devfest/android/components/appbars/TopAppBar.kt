package org.gdglille.devfest.android.components.appbars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.extensions.stringResource
import org.gdglille.devfest.android.theme.Conferences4HallTheme
import org.gdglille.devfest.android.ui.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (AppBarIcons.() -> Unit)? = null,
    actions: List<ActionItem> = emptyList(),
    onActionClicked: ((ActionItemId) -> Unit)? = null
) {
    SmallTopAppBar(
        title = { Text(text = title) },
        modifier = modifier,
        navigationIcon = navigationIcon.takeOrEmpty(),
        actions = {
            actions.forEach { action ->
                IconButton(onClick = { onActionClicked?.let { it(action.id) } }) {
                    Icon(
                        imageVector = action.icon,
                        contentDescription = action.contentDescription?.let {
                            stringResource(id = it, action.formatArgs)
                        } ?: run { null }
                    )
                }
            }
        }
    )
}

open class ActionItem(
    val id: ActionItemId,
    val icon: ImageVector,
    val contentDescription: Int?,
    val formatArgs: List<String> = emptyList()
)

sealed class ActionItemId {
    object FavoriteSchedulesActionItem : ActionItemId()
    object VCardQrCodeScannerActionItem : ActionItemId()
    object QrCodeActionItem : ActionItemId()
    object ShareActionItem : ActionItemId()
    object ReportActionItem : ActionItemId()
}

internal fun (@Composable AppBarIcons.() -> Unit)?.takeOrEmpty(): (@Composable () -> Unit) {
    if (this == null) return {}
    return {
        AppBarIcons.this()
    }
}

@Preview
@Composable
fun TopAppBarPreview() {
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
                        contentDescription = R.string.action_qrcode_scanner,
                        id = ActionItemId.VCardQrCodeScannerActionItem
                    )
                )
            )
        }
    }
}
