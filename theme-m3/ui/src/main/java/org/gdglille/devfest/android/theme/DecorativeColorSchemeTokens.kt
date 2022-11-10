package org.gdglille.devfest.android.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color

internal enum class DecorativeColorSchemeTokens {
    Amethyst,
    OnAmethyst,
    Brick,
    OnBrick,
    Cobalt,
    OnCobalt,
    Emerald,
    OnEmerald,
    Gold,
    OnGold,
    Gravel,
    OnGravel,
    Jade,
    OnJade,
    Saffron,
    OnSaffron
}

@Suppress("ComplexMethod")
internal fun DecorativeColorScheme.fromToken(value: DecorativeColorSchemeTokens): Color {
    return when (value) {
        DecorativeColorSchemeTokens.Amethyst -> amethyst
        DecorativeColorSchemeTokens.OnAmethyst -> onAmethyst
        DecorativeColorSchemeTokens.Brick -> brick
        DecorativeColorSchemeTokens.OnBrick -> onBrick
        DecorativeColorSchemeTokens.Cobalt -> cobalt
        DecorativeColorSchemeTokens.OnCobalt -> onCobalt
        DecorativeColorSchemeTokens.Emerald -> emerald
        DecorativeColorSchemeTokens.OnEmerald -> onEmerald
        DecorativeColorSchemeTokens.Gold -> gold
        DecorativeColorSchemeTokens.OnGold -> onGold
        DecorativeColorSchemeTokens.Gravel -> gravel
        DecorativeColorSchemeTokens.OnGravel -> onGravel
        DecorativeColorSchemeTokens.Jade -> jade
        DecorativeColorSchemeTokens.OnJade -> onJade
        DecorativeColorSchemeTokens.Saffron -> saffron
        DecorativeColorSchemeTokens.OnSaffron -> onSaffron
    }
}

@ReadOnlyComposable
@Composable
internal fun DecorativeColorSchemeTokens.toColor(): Color {
    return LocalDecorativeColorScheme.current.fromToken(this)
}
