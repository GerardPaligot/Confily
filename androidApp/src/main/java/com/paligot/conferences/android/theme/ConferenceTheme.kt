package com.paligot.conferences.android.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun ConferenceTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = darkColors,
        typography = typography,
        content = content
    )
}
