package com.paligot.confily.style.theme.appbars

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.paligot.confily.style.theme.actions.TopAction
import com.paligot.confily.style.theme.actions.TopActionsUi
import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.compose.resources.stringResource

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
