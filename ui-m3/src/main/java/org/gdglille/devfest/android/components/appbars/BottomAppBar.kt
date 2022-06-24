package org.gdglille.devfest.android.components.appbars

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.gdglille.devfest.android.screens.Screen
import org.gdglille.devfest.android.theme.Conferences4HallTheme
import org.gdglille.devfest.android.ui.R

@Composable
fun BottomAppBar(
    selected: Screen?,
    modifier: Modifier = Modifier,
    onClick: (selected: Screen) -> Unit,
) {
    NavigationBar(modifier = modifier) {
        NavigationBarItem(
            selected = Screen.Agenda == selected,
            onClick = {
                onClick(Screen.Agenda)
            },
            icon = {
                Icon(
                    imageVector = Screen.Agenda.imageVector(Screen.Agenda == selected),
                    contentDescription = stringResource(id = Screen.Agenda.title),
                    tint = iconColor(selected = Screen.Agenda == selected)
                )
            },
            label = {
                Text(
                    text = stringResource(id = R.string.screen_agenda),
                    color = labelColor(selected = Screen.Agenda == selected)
                )
            },
            alwaysShowLabel = true
        )
        NavigationBarItem(
            selected = Screen.Networking == selected,
            onClick = {
                onClick(Screen.Networking)
            },
            icon = {
                Icon(
                    imageVector = Screen.Networking.imageVector(Screen.Networking == selected),
                    contentDescription = stringResource(id = Screen.Networking.title),
                    tint = iconColor(selected = Screen.Networking == selected)
                )
            },
            label = {
                Text(
                    text = stringResource(id = R.string.screen_networking),
                    color = labelColor(selected = Screen.Networking == selected)
                )
            },
            alwaysShowLabel = true
        )
        NavigationBarItem(
            selected = Screen.Partners == selected,
            onClick = {
                onClick(Screen.Partners)
            },
            icon = {
                Icon(
                    imageVector = Screen.Partners.imageVector(Screen.Partners == selected),
                    contentDescription = stringResource(id = Screen.Partners.title),
                    tint = iconColor(selected = Screen.Partners == selected)
                )
            },
            label = {
                Text(
                    text = stringResource(id = R.string.screen_partners),
                    color = labelColor(selected = Screen.Partners == selected)
                )
            },
            alwaysShowLabel = true
        )
        NavigationBarItem(
            selected = Screen.Event == selected,
            onClick = {
                onClick(Screen.Event)
            },
            icon = {
                Icon(
                    imageVector = Screen.Event.imageVector(Screen.Event == selected),
                    contentDescription = stringResource(id = Screen.Event.title),
                    tint = iconColor(selected = Screen.Event == selected)
                )
            },
            label = {
                Text(
                    text = stringResource(id = R.string.screen_event),
                    color = labelColor(selected = Screen.Event == selected)
                )
            },
            alwaysShowLabel = true
        )
    }
}

@Composable
fun iconColor(selected: Boolean): Color =
    if (selected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurfaceVariant

@Composable
fun labelColor(selected: Boolean): Color =
    if (selected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant

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
