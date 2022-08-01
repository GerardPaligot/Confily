package org.gdglille.devfest.android.theme.vitamin.ui.theme

import android.content.Context
import android.view.accessibility.AccessibilityManager
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import com.decathlon.vitamin.compose.foundation.VitaminTheme

@Composable
fun Conferences4HallTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalAccessibility provides
            LocalContext.current.getSystemService(Context.ACCESSIBILITY_SERVICE)
            as AccessibilityManager
    ) {
        VitaminTheme(
            darkTheme = useDarkTheme,
            content = content
        )
    }
}
