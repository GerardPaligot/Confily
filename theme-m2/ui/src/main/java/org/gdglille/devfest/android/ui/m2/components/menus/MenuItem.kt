package org.gdglille.devfest.android.ui.m2.components.menus

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.ui.m2.theme.Conferences4HallTheme
import org.gdglille.devfest.android.ui.m2.theme.placeholder
import org.gdglille.devfest.models.MenuItemUi

@Composable
fun MenuItem(
    menuItem: MenuItemUi,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = menuItem.name,
            style = MaterialTheme.typography.subtitle1,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.placeholder(isLoading)
        )
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier
                    .placeholder(isLoading)
                    .semantics(mergeDescendants = true) {}
            ) {
                Text(menuItem.dish)
                Text(menuItem.accompaniment)
                Text(menuItem.dessert)
            }
        }
    }
}

@Preview
@Composable
fun MenuItemPreview() {
    Conferences4HallTheme {
        MenuItem(
            menuItem = MenuItemUi.fake
        )
    }
}
