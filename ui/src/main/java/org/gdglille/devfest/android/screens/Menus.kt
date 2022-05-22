package org.gdglille.devfest.android.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.components.appbars.TopAppBar
import org.gdglille.devfest.android.components.menus.MenuItem
import org.gdglille.devfest.android.theme.Conferences4HallTheme
import org.gdglille.devfest.android.ui.R
import org.gdglille.devfest.models.MenuItemUi

@Composable
fun Menus(
    menuItems: List<MenuItemUi>,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    onBackClicked: () -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = stringResource(id = R.string.screen_menus),
                navigationIcon = { Back(onClick = onBackClicked) }
            )
        },
    ) {
        LazyColumn(
            modifier = modifier.padding(it),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(menuItems) { menuItem ->
                MenuItem(
                    menuItem = menuItem,
                    isLoading = isLoading
                )
            }
        }
    }
}

@Preview
@Composable
fun MenusPreview() {
    Conferences4HallTheme {
        Menus(
            menuItems = arrayListOf(MenuItemUi.fake, MenuItemUi.fake, MenuItemUi.fake)
        ) {}
    }
}
