package org.gdglille.devfest.android.theme.m3.style.speakers.avatar

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color

internal enum class SpeakerAvatarColorTokens {
    PrimaryColor
}

internal fun ColorScheme.fromToken(value: SpeakerAvatarColorTokens): Color {
    return when (value) {
        SpeakerAvatarColorTokens.PrimaryColor -> primary
    }
}

@Composable
@ReadOnlyComposable
internal fun SpeakerAvatarColorTokens.toColor(): Color {
    return MaterialTheme.colorScheme.fromToken(this)
}
