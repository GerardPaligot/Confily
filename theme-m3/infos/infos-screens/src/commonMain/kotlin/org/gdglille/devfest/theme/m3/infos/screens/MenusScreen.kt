package org.gdglille.devfest.theme.m3.infos.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import org.gdglille.devfest.models.ui.MenuItemUi
import org.gdglille.devfest.theme.m3.infos.ui.menus.MenuItem

@ExperimentalMaterial3Api
@Composable
fun MenusScreen(
    menuItems: ImmutableList<MenuItemUi>,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(menuItems) { menuItem ->
            MenuItem(
                menuItem = menuItem,
                isLoading = isLoading
            )
        }
    }
}
