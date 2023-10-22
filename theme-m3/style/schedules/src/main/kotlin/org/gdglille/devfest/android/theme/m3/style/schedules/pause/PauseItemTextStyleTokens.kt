package org.gdglille.devfest.android.theme.m3.style.schedules.pause

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.text.TextStyle

enum class PauseItemTextStyleTokens {
    TitleMedium
}

internal fun Typography.fromToken(value: PauseItemTextStyleTokens): TextStyle {
    return when (value) {
        PauseItemTextStyleTokens.TitleMedium -> titleMedium
    }
}

@Composable
@ReadOnlyComposable
internal fun PauseItemTextStyleTokens.toTextStyle(): TextStyle {
    return MaterialTheme.typography.fromToken(this)
}
