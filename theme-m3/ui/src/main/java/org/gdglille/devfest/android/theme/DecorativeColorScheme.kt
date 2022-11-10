package org.gdglille.devfest.android.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.graphics.Color

@Stable
class DecorativeColorScheme(
    amethyst: Color,
    onAmethyst: Color,
    brick: Color,
    onBrick: Color,
    cobalt: Color,
    onCobalt: Color,
    emerald: Color,
    onEmerald: Color,
    gold: Color,
    onGold: Color,
    gravel: Color,
    onGravel: Color,
    jade: Color,
    onJade: Color,
    saffron: Color,
    onSaffron: Color
) {
    var amethyst by mutableStateOf(amethyst, structuralEqualityPolicy())
        internal set
    var onAmethyst by mutableStateOf(onAmethyst, structuralEqualityPolicy())
        internal set
    var brick by mutableStateOf(brick, structuralEqualityPolicy())
        internal set
    var onBrick by mutableStateOf(onBrick, structuralEqualityPolicy())
        internal set
    var cobalt by mutableStateOf(cobalt, structuralEqualityPolicy())
        internal set
    var onCobalt by mutableStateOf(onCobalt, structuralEqualityPolicy())
        internal set
    var emerald by mutableStateOf(emerald, structuralEqualityPolicy())
        internal set
    var onEmerald by mutableStateOf(onEmerald, structuralEqualityPolicy())
        internal set
    var gold by mutableStateOf(gold, structuralEqualityPolicy())
        internal set
    var onGold by mutableStateOf(onGold, structuralEqualityPolicy())
        internal set
    var gravel by mutableStateOf(gravel, structuralEqualityPolicy())
        internal set
    var onGravel by mutableStateOf(onGravel, structuralEqualityPolicy())
        internal set
    var jade by mutableStateOf(jade, structuralEqualityPolicy())
        internal set
    var onJade by mutableStateOf(onJade, structuralEqualityPolicy())
        internal set
    var saffron by mutableStateOf(saffron, structuralEqualityPolicy())
        internal set
    var onSaffron by mutableStateOf(onSaffron, structuralEqualityPolicy())
        internal set
}

@Suppress("LongParameterList")
fun lightDecorativeColorScheme(
    amethyst: Color = DecorativePaletteTokens.Purple100,
    onAmethyst: Color = DecorativePaletteTokens.Purple500,
    brick: Color = DecorativePaletteTokens.Red100,
    onBrick: Color = DecorativePaletteTokens.Red500,
    cobalt: Color = DecorativePaletteTokens.Blue100,
    onCobalt: Color = DecorativePaletteTokens.Blue500,
    emerald: Color = DecorativePaletteTokens.Green100,
    onEmerald: Color = DecorativePaletteTokens.Green500,
    gold: Color = DecorativePaletteTokens.Yellow100,
    onGold: Color = DecorativePaletteTokens.Yellow500,
    gravel: Color = DecorativePaletteTokens.Grey100,
    onGravel: Color = DecorativePaletteTokens.Grey500,
    jade: Color = DecorativePaletteTokens.Conifer100,
    onJade: Color = DecorativePaletteTokens.Conifer500,
    saffron: Color = DecorativePaletteTokens.Orange100,
    onSaffron: Color = DecorativePaletteTokens.Orange500
): DecorativeColorScheme = DecorativeColorScheme(
    amethyst = amethyst,
    onAmethyst = onAmethyst,
    brick = brick,
    onBrick = onBrick,
    cobalt = cobalt,
    onCobalt = onCobalt,
    emerald = emerald,
    onEmerald = onEmerald,
    gold = gold,
    onGold = onGold,
    gravel = gravel,
    onGravel = onGravel,
    jade = jade,
    onJade = onJade,
    saffron = saffron,
    onSaffron = onSaffron
)

@Suppress("LongParameterList")
fun darkDecorativeColorScheme(
    amethyst: Color = DecorativePaletteTokens.Purple100,
    onAmethyst: Color = DecorativePaletteTokens.Purple600,
    brick: Color = DecorativePaletteTokens.Red100,
    onBrick: Color = DecorativePaletteTokens.Red600,
    cobalt: Color = DecorativePaletteTokens.Blue100,
    onCobalt: Color = DecorativePaletteTokens.Blue600,
    emerald: Color = DecorativePaletteTokens.Green100,
    onEmerald: Color = DecorativePaletteTokens.Green600,
    gold: Color = DecorativePaletteTokens.Yellow100,
    onGold: Color = DecorativePaletteTokens.Yellow600,
    gravel: Color = DecorativePaletteTokens.Grey100,
    onGravel: Color = DecorativePaletteTokens.Grey600,
    jade: Color = DecorativePaletteTokens.Conifer100,
    onJade: Color = DecorativePaletteTokens.Conifer600,
    saffron: Color = DecorativePaletteTokens.Orange100,
    onSaffron: Color = DecorativePaletteTokens.Orange600
): DecorativeColorScheme = DecorativeColorScheme(
    amethyst = amethyst,
    onAmethyst = onAmethyst,
    brick = brick,
    onBrick = onBrick,
    cobalt = cobalt,
    onCobalt = onCobalt,
    emerald = emerald,
    onEmerald = onEmerald,
    gold = gold,
    onGold = onGold,
    gravel = gravel,
    onGravel = onGravel,
    jade = jade,
    onJade = onJade,
    saffron = saffron,
    onSaffron = onSaffron
)
