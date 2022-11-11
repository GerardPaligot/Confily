package org.gdglille.devfest.android.components.menus

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.theme.Conferences4HallTheme
import org.gdglille.devfest.android.theme.placeholder
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
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.placeholder(isLoading)
        )
        CompositionLocalProvider(LocalContentColor provides LocalContentColor.current.copy(alpha = .74f)) {
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
