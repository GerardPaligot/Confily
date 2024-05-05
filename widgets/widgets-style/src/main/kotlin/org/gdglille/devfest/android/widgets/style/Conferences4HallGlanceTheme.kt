package org.gdglille.devfest.android.widgets.style

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.glance.GlanceComposable
import androidx.glance.GlanceTheme

@Composable
fun Conferences4HallGlanceTheme(
    content: @GlanceComposable
    @Composable
    () -> Unit
) {
    GlanceTheme(
        colors = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            GlanceTheme.colors
        } else {
            Conferences4HallGlanceColorScheme.colors
        },
        content = content
    )
}
