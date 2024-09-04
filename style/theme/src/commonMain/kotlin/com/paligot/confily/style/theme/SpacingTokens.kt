package com.paligot.confily.style.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class SpacingTokens {
    None,
    ExtraSmallSpacing,
    SmallSpacing,
    MediumSpacing,
    LargeSpacing,
    ExtraLargeSpacing
}

internal fun fromToken(value: SpacingTokens): Dp {
    return when (value) {
        SpacingTokens.None -> 0.dp
        SpacingTokens.ExtraSmallSpacing -> 2.dp
        SpacingTokens.SmallSpacing -> 4.dp
        SpacingTokens.MediumSpacing -> 8.dp
        SpacingTokens.LargeSpacing -> 16.dp
        SpacingTokens.ExtraLargeSpacing -> 32.dp
    }
}

@Composable
@ReadOnlyComposable
fun SpacingTokens.toDp(): Dp {
    return fromToken(this)
}
