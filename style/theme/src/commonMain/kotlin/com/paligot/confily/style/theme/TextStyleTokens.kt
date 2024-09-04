package com.paligot.confily.style.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

enum class TextStyleTokens {
    HeadlineLarge,
    TitleLarge,
    TitleMedium,
    TitleSmall,
    BodySmall,
    BodyMedium,
    BoldBodyMedium
}

internal fun Typography.fromToken(value: TextStyleTokens): TextStyle {
    return when (value) {
        TextStyleTokens.HeadlineLarge -> headlineLarge
        TextStyleTokens.TitleLarge -> titleLarge
        TextStyleTokens.TitleMedium -> titleMedium
        TextStyleTokens.TitleSmall -> titleSmall
        TextStyleTokens.BodySmall -> bodySmall
        TextStyleTokens.BodyMedium -> bodyMedium
        TextStyleTokens.BoldBodyMedium -> bodyMedium.copy(fontWeight = FontWeight.Bold)
    }
}

@Composable
@ReadOnlyComposable
fun TextStyleTokens.toTextStyle(): TextStyle {
    return MaterialTheme.typography.fromToken(this)
}
