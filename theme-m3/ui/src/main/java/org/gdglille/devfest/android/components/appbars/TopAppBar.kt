package org.gdglille.devfest.android.components.appbars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import org.gdglille.devfest.android.components.structure.OverflowMenu
import org.gdglille.devfest.android.theme.Conferences4HallTheme
import org.gdglille.devfest.android.ui.resources.R
import org.gdglille.devfest.android.ui.resources.actions.TopAction
import org.gdglille.devfest.android.ui.resources.models.TopActionsUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (AppBarIcons.() -> Unit)? = null,
    topActionsUi: TopActionsUi = TopActionsUi(),
    onActionClicked: (TopAction) -> Unit = { }
) {
    val showAsActionItems = topActionsUi.actions.take(topActionsUi.maxActions)
    val overflowActions = TopActionsUi(
        topActionsUi.actions.subtract(showAsActionItems.toSet()).toImmutableList()
    )
    SmallTopAppBar(
        title = { Text(text = title) },
        modifier = modifier,
        navigationIcon = navigationIcon.takeOrEmpty(),
        actions = {
            showAsActionItems.forEach { action ->
                IconButton(onClick = { onActionClicked(action) }) {
                    Icon(
                        painter = painterResource(action.icon),
                        contentDescription = action.contentDescription?.let {
                            stringResource(id = it)
                        }
                    )
                }
            }
            if (overflowActions.actions.isNotEmpty()) {
                OverflowMenu(
                    topActionsUi = overflowActions,
                    onClick = onActionClicked
                )
            }
        }
    )
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
                topActionsUi = TopActionsUi(
                    actions = persistentListOf(
                        TopAction(
                            id = 0,
                            icon = R.drawable.ic_mtrl_qr_code_scanner_line,
                            contentDescription = R.string.action_qrcode_scanner
                        )
                    )
                )
            )
        }
    }
}
