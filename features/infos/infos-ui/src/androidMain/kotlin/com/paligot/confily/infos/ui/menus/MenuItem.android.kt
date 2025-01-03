package com.paligot.confily.infos.ui.menus

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.infos.ui.models.MenuItemUi
import com.paligot.confily.style.theme.ConfilyTheme

@Preview
@Composable
private fun MenuItemPreview() {
    ConfilyTheme {
        MenuItem(
            menuItem = MenuItemUi.fake
        )
    }
}
