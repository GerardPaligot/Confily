package com.paligot.conferences.android.components.appbars

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.conferences.android.theme.Conferences4HallTheme

enum class ItemSelected { Agenda, Event, None }

@Composable
fun BottomAppBar(
    selected: ItemSelected,
    modifier: Modifier = Modifier,
    onClick: (selected: ItemSelected) -> Unit,
) {
    BottomNavigation(modifier = modifier) {
        BottomNavigationItem(
            selected = ItemSelected.Agenda == selected,
            onClick = {
                onClick(ItemSelected.Agenda)
            },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Event,
                    contentDescription = "Agenda"
                )
            },
            alwaysShowLabel = false
        )
        BottomNavigationItem(
            selected = ItemSelected.Event == selected,
            onClick = {
                onClick(ItemSelected.Event)
            },
            icon = {
                Icon(
                    imageVector = Icons.Filled.LocalActivity,
                    contentDescription = "Event Info"
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
            selected = ItemSelected.None,
            onClick = {}
        )
    }
}
