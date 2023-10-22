package org.gdglille.devfest.android.theme.m3.style

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class SpacingTokens {
    ExtraSmallSpacing,
    SmallSpacing,
    MediumSpacing,
    LargeSpacing
}

internal fun fromToken(value: SpacingTokens): Dp {
    return when (value) {
        SpacingTokens.ExtraSmallSpacing -> 2.dp
        SpacingTokens.SmallSpacing -> 4.dp
        SpacingTokens.MediumSpacing -> 8.dp
        SpacingTokens.LargeSpacing -> 16.dp
    }
}

@Composable
@ReadOnlyComposable
fun SpacingTokens.toDp(): Dp {
    return fromToken(this)
}
