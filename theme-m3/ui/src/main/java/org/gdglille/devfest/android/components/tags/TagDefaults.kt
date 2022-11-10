package org.gdglille.devfest.android.components.tags

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.theme.toColor

object TagDefaults {
    private val HorizontalPadding = 8.dp
    private val VerticalPadding = 6.dp

    val IconSize = 18.dp

    val IconTextPadding = 4.dp

    val ContentPadding = PaddingValues(
        start = HorizontalPadding,
        top = VerticalPadding,
        end = HorizontalPadding,
        bottom = VerticalPadding
    )

    val shape: Shape @Composable get() = MaterialTheme.shapes.small

    val textStyle: TextStyle @Composable get() = MaterialTheme.typography.bodySmall

    @Composable
    fun amethystColors(
        containerColor: Color = AmethystTagTokens.ContainerColor.toColor(),
        contentColor: Color = AmethystTagTokens.ContentColor.toColor()
    ): TagColors = remember(containerColor, contentColor) {
        TagColors(containerColor, contentColor)
    }

    @Composable
    fun brickColors(
        containerColor: Color = BrickTagTokens.ContainerColor.toColor(),
        contentColor: Color = BrickTagTokens.ContentColor.toColor()
    ): TagColors = remember(containerColor, contentColor) {
        TagColors(containerColor, contentColor)
    }

    @Composable
    fun cobaltColors(
        containerColor: Color = CobaltTagTokens.ContainerColor.toColor(),
        contentColor: Color = CobaltTagTokens.ContentColor.toColor()
    ): TagColors = remember(containerColor, contentColor) {
        TagColors(containerColor, contentColor)
    }

    @Composable
    fun emeraldColors(
        containerColor: Color = EmeraldTagTokens.ContainerColor.toColor(),
        contentColor: Color = EmeraldTagTokens.ContentColor.toColor()
    ): TagColors = remember(containerColor, contentColor) {
        TagColors(containerColor, contentColor)
    }

    @Composable
    fun goldColors(
        containerColor: Color = GoldTagTokens.ContainerColor.toColor(),
        contentColor: Color = GoldTagTokens.ContentColor.toColor()
    ): TagColors = remember(containerColor, contentColor) {
        TagColors(containerColor, contentColor)
    }

    @Composable
    fun gravelColors(
        containerColor: Color = GravelTagTokens.ContainerColor.toColor(),
        contentColor: Color = GravelTagTokens.ContentColor.toColor()
    ): TagColors = remember(containerColor, contentColor) {
        TagColors(containerColor, contentColor)
    }

    @Composable
    fun jadeColors(
        containerColor: Color = JadeTagTokens.ContainerColor.toColor(),
        contentColor: Color = JadeTagTokens.ContentColor.toColor()
    ): TagColors = remember(containerColor, contentColor) {
        TagColors(containerColor, contentColor)
    }

    @Composable
    fun saffronColors(
        containerColor: Color = SaffronTagTokens.ContainerColor.toColor(),
        contentColor: Color = SaffronTagTokens.ContentColor.toColor()
    ): TagColors = remember(containerColor, contentColor) {
        TagColors(containerColor, contentColor)
    }

    @Composable
    fun unStyledColors(
        containerColor: Color = Color.Unspecified,
        contentColor: Color = MaterialTheme.colorScheme.onSurface
    ): TagColors = remember(containerColor, contentColor) {
        TagColors(containerColor, contentColor)
    }
}
