package com.paligot.conferences.ui.components.appbars

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.conferences.ui.screens.Screen
import com.paligot.conferences.ui.theme.Conferences4HallTheme

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
                    contentDescription = Screen.Agenda.title
                )
            },
            label = { Text("Agenda") },
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
                    contentDescription = Screen.Networking.title
                )
            },
            label = { Text("Networking") },
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
                    contentDescription = Screen.Event.title
                )
            },
            label = { Text("Event") },
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
