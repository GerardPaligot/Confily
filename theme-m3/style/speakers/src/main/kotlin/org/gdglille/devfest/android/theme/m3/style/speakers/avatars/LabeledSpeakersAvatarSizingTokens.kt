package org.gdglille.devfest.android.theme.m3.style.speakers.avatars

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class LabeledSpeakersAvatarSizingTokens {
    SmallSpacing
}

internal fun fromToken(value: LabeledSpeakersAvatarSizingTokens): Dp {
    return when (value) {
        LabeledSpeakersAvatarSizingTokens.SmallSpacing -> 8.dp
    }
}

@Composable
@ReadOnlyComposable
internal fun LabeledSpeakersAvatarSizingTokens.toDp(): Dp {
    return fromToken(this)
}
