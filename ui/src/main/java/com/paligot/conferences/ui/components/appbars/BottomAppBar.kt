package com.paligot.conferences.ui.components.appbars

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
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
                    imageVector = Screen.Agenda.imageVector,
                    contentDescription = Screen.Agenda.title
                )
            },
            alwaysShowLabel = false
        )
        BottomNavigationItem(
            selected = Screen.Event == selected,
            onClick = {
                onClick(Screen.Event)
            },
            icon = {
                Icon(
                    imageVector = Screen.Event.imageVector,
                    contentDescription = Screen.Event.title
                )
            },
            alwaysShowLabel = false
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
