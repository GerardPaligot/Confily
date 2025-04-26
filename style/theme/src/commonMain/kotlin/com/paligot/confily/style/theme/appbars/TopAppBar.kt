package com.paligot.confily.style.theme.appbars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.action_qrcode_scanner
import com.paligot.confily.style.theme.ConfilyTheme
import com.paligot.confily.style.theme.actions.TopAction
import com.paligot.confily.style.theme.actions.TopActionsUi
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
    navigationIcon: @Composable (AppBarIcons.() -> Unit)? = null,
    topActionsUi: TopActionsUi = TopActionsUi(),
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onActionClicked: (TopAction) -> Unit = { }
) {
    val showAsActionItems = topActionsUi.actions.take(topActionsUi.maxActions)
    val overflowActions = TopActionsUi(
        topActionsUi.actions.subtract(showAsActionItems.toSet()).toImmutableList()
    )
    androidx.compose.material3.TopAppBar(
        title = { Text(text = title) },
        modifier = modifier,
        navigationIcon = {
            navigationIcon?.let { AppBarIcons.it() }
        },
        actions = {
            showAsActionItems.forEach { action ->
                IconButton(onClick = { onActionClicked(action) }) {
                    Icon(
                        imageVector = action.icon,
                        contentDescription = action.contentDescription?.let {
                            stringResource(it)
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
        },
        scrollBehavior = scrollBehavior,
        colors = colors
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun TopAppBarPreview() {
    ConfilyTheme {
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
                            icon = Icons.Outlined.QrCodeScanner,
                            contentDescription = Resource.string.action_qrcode_scanner
                        )
                    )
                )
            )
        }
    }
}
