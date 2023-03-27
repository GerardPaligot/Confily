package org.gdglille.devfest.android.theme.vitamin.ui.screens.event

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decathlon.vitamin.compose.dividers.VitaminDividers
import com.decathlon.vitamin.compose.foundation.VitaminTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.gdglille.devfest.android.theme.vitamin.ui.components.menus.MenuItem
import org.gdglille.devfest.android.theme.vitamin.ui.theme.Conferences4HallTheme
import org.gdglille.devfest.android.ui.resources.R
import org.gdglille.devfest.models.MenuItemUi

@Composable
fun Menus(
    menuItems: ImmutableList<MenuItemUi>,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(start = 16.dp, top = 24.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = stringResource(R.string.screen_menus),
                style = VitaminTheme.typography.h4,
                color = VitaminTheme.colors.vtmnContentPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        items(menuItems) { menuItem ->
            MenuItem(
                menuItem = menuItem,
                isLoading = isLoading
            )
            Spacer(modifier = Modifier.height(16.dp))
            VitaminDividers.FullBleed(modifier = Modifier.fillMaxWidth())
        }
    }
}

@Preview
@Composable
fun MenusPreview() {
    Conferences4HallTheme {
        Menus(
            menuItems = persistentListOf(MenuItemUi.fake, MenuItemUi.fake, MenuItemUi.fake)
        )
    }
}
