package org.gdglille.devfest.android.components.appbars

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.gdglille.devfest.android.theme.Conferences4HallTheme
import org.gdglille.devfest.android.ui.resources.actions.BottomAction
import org.gdglille.devfest.android.ui.resources.models.BottomActionsUi

@Composable
fun BottomAppBar(
    bottomActions: BottomActionsUi,
    routeSelected: String,
    modifier: Modifier = Modifier,
    onClick: (BottomAction) -> Unit,
) {
    NavigationBar(modifier = modifier) {
        bottomActions.actions.forEach { action ->
            val selected = action.route == routeSelected
            NavigationBarItem(
                selected = selected,
                onClick = {
                    onClick(action)
                },
                icon = {
                    Icon(
                        painter = painterResource(if (selected) action.iconSelected else action.icon),
                        contentDescription = action.contentDescription?.let { stringResource(it) },
                        tint = iconColor(selected = selected)
                    )
                },
                alwaysShowLabel = false
            )
        }
    }
}

@Composable
fun iconColor(selected: Boolean): Color =
    if (selected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurfaceVariant

@Preview
@Composable
fun BottomAppBarPreview() {
    Conferences4HallTheme {
        BottomAppBar(
            bottomActions = BottomActionsUi(),
            routeSelected = "",
            onClick = {}
        )
    }
}
