package org.gdglille.devfest.android.theme.m3.infos.screens

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.collections.immutable.persistentListOf
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.models.ui.MenuItemUi
import org.gdglille.devfest.theme.m3.infos.screens.MenusScreen

@ExperimentalMaterial3Api
@Preview
@Composable
private fun MenusPreview() {
    Conferences4HallTheme {
        MenusScreen(
            menuItems = persistentListOf(MenuItemUi.fake, MenuItemUi.fake, MenuItemUi.fake)
        )
    }
}
