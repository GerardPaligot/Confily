package org.gdglille.devfest.android.theme.m3.style.tags

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.theme.m3.style.TextStyleTokens
import org.gdglille.devfest.android.theme.m3.style.toColor
import org.gdglille.devfest.android.theme.m3.style.toDp
import org.gdglille.devfest.android.theme.m3.style.toShape
import org.gdglille.devfest.android.theme.m3.style.toTextStyle

object TagDefaults {
    val iconSize = 18.dp
    val textStyle: TextStyle @Composable get() = TextStyleTokens.BodySmall.toTextStyle()

    val mediumContentPadding: PaddingValues
        @Composable get() = PaddingValues(
            horizontal = TagMediumTokens.ContentHorizontalPadding.toDp(),
            vertical = TagMediumTokens.ContentVerticalPadding.toDp()
        )
    val mediumTextPadding: Dp
        @Composable get() = TagMediumTokens.BetweenSpacing.toDp()
    val mediumShape: Shape @Composable get() = TagMediumTokens.ContainerShape.toShape()

    val smallContentPadding: PaddingValues
        @Composable get() = PaddingValues(
            horizontal = TagSmallTokens.ContentHorizontalPadding.toDp(),
            vertical = TagSmallTokens.ContentVerticalPadding.toDp()
        )
    val smallTextPadding: Dp
        @Composable get() = TagSmallTokens.BetweenSpacing.toDp()
    val smallShape: Shape @Composable get() = TagSmallTokens.ContainerShape.toShape()

    @Composable
    fun colors(colorName: String): TagColors {
        return when (colorName) {
            "amethyst" -> amethystColors()
            "brick" -> brickColors()
            "cobalt" -> cobaltColors()
            "emerald" -> emeraldColors()
            "gold" -> goldColors()
            "gravel" -> gravelColors()
            "jade" -> jadeColors()
            "saffron" -> saffronColors()
            else -> unStyledColors()
        }
    }

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
