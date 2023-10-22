package org.gdglille.devfest.android.theme.m3.style.schedules.card

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

internal enum class ScheduleCardSizingTokens {
    SmallSpacing,
    MediumSpacing,
    SmallPadding,
    MediumPadding
}

internal fun fromToken(value: ScheduleCardSizingTokens): Dp {
    return when (value) {
        ScheduleCardSizingTokens.SmallSpacing -> 4.dp
        ScheduleCardSizingTokens.MediumSpacing -> 8.dp
        ScheduleCardSizingTokens.SmallPadding -> 4.dp
        ScheduleCardSizingTokens.MediumPadding -> 16.dp
    }
}

@Composable
@ReadOnlyComposable
internal fun ScheduleCardSizingTokens.toDp(): Dp {
    return fromToken(this)
}
