package org.gdglille.devfest.android.ui.m2.components.appbars

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.gdglille.devfest.android.ui.m2.R
import org.gdglille.devfest.android.ui.m2.screens.Screen
import org.gdglille.devfest.android.ui.m2.theme.Conferences4HallTheme

@Composable
fun BottomAppBar(
    selected: Screen?,
    modifier: Modifier = Modifier,
    onClick: (selected: Screen) -> Unit,
) {
    BottomNavigation(modifier = modifier) {
        BottomNavigationItem(
            selected = Screen.Agenda == selected,
            onClick = {
                onClick(Screen.Agenda)
            },
            icon = {
                Icon(
                    imageVector = Screen.Agenda.imageVector(Screen.Agenda == selected),
                    contentDescription = stringResource(id = Screen.Agenda.title)
                )
            },
            label = { Text(text = stringResource(id = R.string.screen_agenda)) },
            alwaysShowLabel = true
        )
        BottomNavigationItem(
            selected = Screen.Networking == selected,
            onClick = {
                onClick(Screen.Networking)
            },
            icon = {
                Icon(
                    imageVector = Screen.Networking.imageVector(Screen.Networking == selected),
                    contentDescription = stringResource(id = Screen.Networking.title)
                )
            },
            label = { Text(text = stringResource(id = R.string.screen_networking)) },
            alwaysShowLabel = true
        )
        BottomNavigationItem(
            selected = Screen.Partners == selected,
            onClick = {
                onClick(Screen.Partners)
            },
            icon = {
                Icon(
                    imageVector = Screen.Partners.imageVector(Screen.Partners == selected),
                    contentDescription = stringResource(id = Screen.Partners.title)
                )
            },
            label = { Text(text = stringResource(id = R.string.screen_partners)) },
            alwaysShowLabel = true
        )
        BottomNavigationItem(
            selected = Screen.Event == selected,
            onClick = {
                onClick(Screen.Event)
            },
            icon = {
                Icon(
                    imageVector = Screen.Event.imageVector(Screen.Event == selected),
                    contentDescription = stringResource(id = Screen.Event.title)
                )
            },
            label = { Text(text = stringResource(id = R.string.screen_event)) },
            alwaysShowLabel = true
        )
    }
}

@Preview
@Composable
fun BottomAppBarPreview() {
    Conferences4HallTheme {
        BottomAppBar(
            selected = null,
            onClick = {}
        )
    }
}
