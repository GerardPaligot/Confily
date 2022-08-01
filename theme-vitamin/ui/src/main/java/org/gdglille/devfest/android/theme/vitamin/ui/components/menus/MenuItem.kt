package org.gdglille.devfest.android.theme.vitamin.ui.components.menus

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decathlon.vitamin.compose.foundation.VitaminTheme
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.RichText
import com.halilibo.richtext.ui.RichTextThemeIntegration
import org.gdglille.devfest.android.theme.vitamin.ui.theme.Conferences4HallTheme
import org.gdglille.devfest.android.theme.vitamin.ui.theme.placeholder
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
            style = VitaminTheme.typography.h6,
            fontWeight = FontWeight.Bold,
            color = VitaminTheme.colors.vtmnContentPrimary,
            modifier = Modifier.placeholder(isLoading)
        )
        RichTextThemeIntegration(
            textStyle = { VitaminTheme.typography.body3 },
            ProvideTextStyle = null,
            contentColor = { VitaminTheme.colors.vtmnContentPrimary },
            ProvideContentColor = null,
        ) {
            RichText(
                modifier = Modifier
                    .placeholder(isLoading)
                    .semantics(mergeDescendants = true) {}
            ) {
                Markdown(
                    """
* ${menuItem.dish}
* ${menuItem.accompaniment}
* ${menuItem.dessert}
                    """.trimIndent()
                )
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
