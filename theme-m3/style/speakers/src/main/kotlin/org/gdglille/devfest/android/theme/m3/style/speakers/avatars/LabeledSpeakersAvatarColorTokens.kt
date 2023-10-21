package org.gdglille.devfest.android.theme.m3.style.speakers.avatars

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color

enum class LabeledSpeakersAvatarColorTokens {
    OnSurface
}

internal fun ColorScheme.fromToken(value: LabeledSpeakersAvatarColorTokens): Color {
    return when (value) {
        LabeledSpeakersAvatarColorTokens.OnSurface -> onSurface
    }
}

@Composable
@ReadOnlyComposable
internal fun LabeledSpeakersAvatarColorTokens.toColor(): Color {
    return MaterialTheme.colorScheme.fromToken(this)
}
