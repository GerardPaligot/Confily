package org.gdglille.devfest.android.theme.m3.style.speakers.avatars

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.text.TextStyle

enum class LabeledSpeakersAvatarTextStyleTokens {
    BodyMedium
}

internal fun Typography.fromToken(value: LabeledSpeakersAvatarTextStyleTokens): TextStyle {
    return when (value) {
        LabeledSpeakersAvatarTextStyleTokens.BodyMedium -> bodyMedium
    }
}

@Composable
@ReadOnlyComposable
internal fun LabeledSpeakersAvatarTextStyleTokens.toTextStyle(): TextStyle {
    return MaterialTheme.typography.fromToken(this)
}
