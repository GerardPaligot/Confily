package org.gdglille.devfest.android.theme.m3.style.schedules.pause

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Shape

internal enum class PauseItemShapeTokens {
    ContainerShape
}

internal fun Shapes.fromToken(value: PauseItemShapeTokens): Shape {
    return when (value) {
        PauseItemShapeTokens.ContainerShape -> medium
    }
}

@Composable
@ReadOnlyComposable
internal fun PauseItemShapeTokens.toShape(): Shape {
    return MaterialTheme.shapes.fromToken(this)
}
