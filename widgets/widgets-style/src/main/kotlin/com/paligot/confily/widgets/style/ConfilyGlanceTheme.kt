package com.paligot.confily.widgets.style

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.glance.GlanceComposable
import androidx.glance.GlanceTheme

@Composable
fun ConfilyGlanceTheme(
    content: @GlanceComposable
    @Composable
    () -> Unit
) {
    GlanceTheme(
        colors = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            GlanceTheme.colors
        } else {
            ConfilyGlanceColorScheme.colors
        },
        content = content
    )
}
