package org.gdglille.devfest.android.theme.m3.infos.ui.menus

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.models.ui.MenuItemUi
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.theme.m3.infos.ui.menus.MenuItem

@Preview
@Composable
private fun MenuItemPreview() {
    Conferences4HallTheme {
        MenuItem(
            menuItem = MenuItemUi.fake
        )
    }
}
