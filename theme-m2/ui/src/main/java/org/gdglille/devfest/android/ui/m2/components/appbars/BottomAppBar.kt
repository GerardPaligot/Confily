package org.gdglille.devfest.android.ui.m2.components.appbars

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.gdglille.devfest.android.ui.m2.theme.Conferences4HallTheme
import org.gdglille.devfest.android.ui.resources.actions.BottomAction
import org.gdglille.devfest.android.ui.resources.models.BottomActionsUi

@Composable
fun BottomAppBar(
    bottomActions: BottomActionsUi,
    routeSelected: String,
    modifier: Modifier = Modifier,
    onClick: (BottomAction) -> Unit,
) {
    BottomNavigation(modifier = modifier) {
        bottomActions.actions.forEach { action ->
            val selected = action.route == routeSelected
            BottomNavigationItem(
                selected = selected,
                onClick = {
                    onClick(action)
                },
                icon = {
                    Icon(
                        painter = painterResource(if (selected) action.iconSelected else action.icon),
                        contentDescription = action.contentDescription?.let { stringResource(it) }
                    )
                },
                label = { Text(text = stringResource(id = action.label)) },
                alwaysShowLabel = false
            )
        }
    }
}

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
