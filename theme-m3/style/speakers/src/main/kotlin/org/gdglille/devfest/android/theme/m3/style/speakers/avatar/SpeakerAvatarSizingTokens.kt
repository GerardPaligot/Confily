package org.gdglille.devfest.android.theme.m3.style.speakers.avatar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

internal enum class SpeakerAvatarSizingTokens {
    SmallBorderWidth,
    MediumBorderWidth,
    LargeBorderWidth
}

internal fun fromToken(value: SpeakerAvatarSizingTokens): Dp {
    return when (value) {
        SpeakerAvatarSizingTokens.SmallBorderWidth -> 1.dp
        SpeakerAvatarSizingTokens.MediumBorderWidth -> 1.dp
        SpeakerAvatarSizingTokens.LargeBorderWidth -> 1.dp
    }
}

@Composable
@ReadOnlyComposable
internal fun SpeakerAvatarSizingTokens.toDp(): Dp {
    return fromToken(this)
}
