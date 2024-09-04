package com.paligot.confily.style.theme.appbars

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.action_expanded_menu
import com.paligot.confily.style.theme.actions.TopAction
import com.paligot.confily.style.theme.actions.TopActionsUi
import com.paligot.confily.style.theme.menus.Dropdown
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun OverflowMenu(
    topActionsUi: TopActionsUi,
    modifier: Modifier = Modifier,
    onClick: (TopAction) -> Unit
) {
    val expanded: MutableState<Boolean> = remember { mutableStateOf(false) }
    Dropdown(
        expanded = expanded,
        modifier = modifier,
        onDismissRequest = { expanded.value = false },
        anchor = {
            IconButton(onClick = { expanded.value = true }) {
                Icon(
                    imageVector = Icons.Outlined.MoreVert,
                    contentDescription = stringResource(Resource.string.action_expanded_menu)
                )
            }
        },
        children = {
            topActionsUi.actions.forEach {
                DropdownMenuItem(
                    text = { it.contentDescription?.let { Text(text = stringResource(it)) } },
                    onClick = {
                        onClick(it)
                        expanded.value = false
                    }
                )
            }
        }
    )
}
