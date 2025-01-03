package com.paligot.confily.infos.panes

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.infos.ui.models.MenuItemUi
import com.paligot.confily.style.theme.ConfilyTheme
import kotlinx.collections.immutable.persistentListOf

@ExperimentalMaterial3Api
@Preview
@Composable
private fun MenusPreview() {
    ConfilyTheme {
        MenusScreen(
            menuItems = persistentListOf(MenuItemUi.fake, MenuItemUi.fake, MenuItemUi.fake)
        )
    }
}
