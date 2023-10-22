package org.gdglille.devfest.android.theme.m3.style.schedules.card

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.text.TextStyle

enum class ScheduleCardTextStyleTokens {
    TitleMedium
}

internal fun Typography.fromToken(value: ScheduleCardTextStyleTokens): TextStyle {
    return when (value) {
        ScheduleCardTextStyleTokens.TitleMedium -> titleMedium
    }
}

@Composable
@ReadOnlyComposable
internal fun ScheduleCardTextStyleTokens.toTextStyle(): TextStyle {
    return MaterialTheme.typography.fromToken(this)
}
