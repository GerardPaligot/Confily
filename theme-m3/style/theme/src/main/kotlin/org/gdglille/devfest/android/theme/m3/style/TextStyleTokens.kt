package org.gdglille.devfest.android.theme.m3.style

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.text.TextStyle

enum class TextStyleTokens {
    TitleMedium,
    BodyMedium
}

internal fun Typography.fromToken(value: TextStyleTokens): TextStyle {
    return when (value) {
        TextStyleTokens.TitleMedium -> titleMedium
        TextStyleTokens.BodyMedium -> bodyMedium
    }
}

@Composable
@ReadOnlyComposable
fun TextStyleTokens.toTextStyle(): TextStyle {
    return MaterialTheme.typography.fromToken(this)
}
