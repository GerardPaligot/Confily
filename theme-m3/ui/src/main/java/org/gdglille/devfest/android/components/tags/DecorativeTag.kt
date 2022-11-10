package org.gdglille.devfest.android.components.tags

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Autorenew
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.Computer
import androidx.compose.material.icons.outlined.DataObject
import androidx.compose.material.icons.outlined.Draw
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.outlined.Smartphone
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import org.gdglille.devfest.models.CategoryUi

@Composable
fun DecorativeTag(
    category: CategoryUi,
    modifier: Modifier = Modifier
) {
    when (category.color) {
        "amethyst" -> Tag(
            text = category.name,
            icon = category.icon?.iconVector(),
            modifier = modifier,
            colors = TagDefaults.amethystColors()
        )

        "cobalt" -> Tag(
            text = category.name,
            icon = category.icon?.iconVector(),
            modifier = modifier,
            colors = TagDefaults.cobaltColors()
        )

        "brick" -> Tag(
            text = category.name,
            icon = category.icon?.iconVector(),
            modifier = modifier,
            colors = TagDefaults.brickColors()
        )

        "emerald" -> Tag(
            text = category.name,
            icon = category.icon?.iconVector(),
            modifier = modifier,
            colors = TagDefaults.emeraldColors()
        )

        "jade" -> Tag(
            text = category.name,
            icon = category.icon?.iconVector(),
            modifier = modifier,
            colors = TagDefaults.jadeColors()
        )

        "saffron" -> Tag(
            text = category.name,
            icon = category.icon?.iconVector(),
            modifier = modifier,
            colors = TagDefaults.saffronColors()
        )

        "gold" -> Tag(
            text = category.name,
            icon = category.icon?.iconVector(),
            modifier = modifier,
            colors = TagDefaults.goldColors()
        )

        "gravel" -> Tag(
            text = category.name,
            icon = category.icon?.iconVector(),
            modifier = modifier,
            colors = TagDefaults.gravelColors()
        )

        "default" -> Tag(
            text = category.name,
            icon = category.icon?.iconVector(),
            modifier = modifier,
            colors = TagDefaults.emeraldColors()
        )
    }
}

@Composable
private fun String.iconVector(): ImageVector? = when (this) {
    "database" -> Icons.Outlined.DataObject
    "computer" -> Icons.Outlined.Computer
    "public" -> Icons.Outlined.Public
    "cloud" -> Icons.Outlined.Cloud
    "smartphone" -> Icons.Outlined.Smartphone
    "lock" -> Icons.Outlined.Lock
    "autorenew" -> Icons.Outlined.Autorenew
    "psychology" -> Icons.Outlined.Psychology
    "draw" -> Icons.Outlined.Draw
    "language" -> Icons.Outlined.Language
    else -> null
}
