package org.gdglille.devfest.android.theme.m3.infos.feature

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.gdglille.devfest.android.theme.m3.infos.ui.menus.MenuItem
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.models.ui.MenuItemUi

@ExperimentalMaterial3Api
@Composable
fun Menus(
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

@ExperimentalMaterial3Api
@Preview
@Composable
private fun MenusPreview() {
    Conferences4HallTheme {
        Menus(
            menuItems = persistentListOf(MenuItemUi.fake, MenuItemUi.fake, MenuItemUi.fake)
        )
    }
}
