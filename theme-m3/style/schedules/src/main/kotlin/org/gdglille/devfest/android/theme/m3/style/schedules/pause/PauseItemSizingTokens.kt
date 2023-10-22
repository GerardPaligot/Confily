package org.gdglille.devfest.android.theme.m3.style.schedules.pause

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

internal enum class PauseItemSizingTokens {
    SmallSpacing,
    SmallPadding
}

internal fun fromToken(value: PauseItemSizingTokens): Dp {
    return when (value) {
        PauseItemSizingTokens.SmallSpacing -> 8.dp
        PauseItemSizingTokens.SmallPadding -> 16.dp
    }
}

@Composable
@ReadOnlyComposable
internal fun PauseItemSizingTokens.toDp(): Dp {
    return fromToken(this)
}
