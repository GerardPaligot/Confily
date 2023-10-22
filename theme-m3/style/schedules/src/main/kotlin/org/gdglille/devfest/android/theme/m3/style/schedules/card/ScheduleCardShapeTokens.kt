package org.gdglille.devfest.android.theme.m3.style.schedules.card

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Shape

internal enum class ScheduleCardShapeTokens {
    ContainerShape
}

internal fun Shapes.fromToken(value: ScheduleCardShapeTokens): Shape {
    return when (value) {
        ScheduleCardShapeTokens.ContainerShape -> medium
    }
}

@Composable
@ReadOnlyComposable
internal fun ScheduleCardShapeTokens.toShape(): Shape {
    return MaterialTheme.shapes.fromToken(this)
}
