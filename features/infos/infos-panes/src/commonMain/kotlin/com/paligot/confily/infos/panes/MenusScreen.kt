package com.paligot.confily.infos.panes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.paligot.confily.infos.ui.menus.MenuItem
import com.paligot.confily.infos.ui.models.MenuItemUi
import kotlinx.collections.immutable.ImmutableList

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
