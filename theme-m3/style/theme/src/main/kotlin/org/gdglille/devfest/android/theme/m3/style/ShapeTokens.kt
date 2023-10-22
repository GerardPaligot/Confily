package org.gdglille.devfest.android.theme.m3.style

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

enum class ShapeTokens {
    CircleShape,
    ExtraSmallTopShape,
    MediumShape
}

internal fun Shapes.fromToken(value: ShapeTokens): Shape {
    return when (value) {
        ShapeTokens.CircleShape -> CircleShape
        ShapeTokens.ExtraSmallTopShape -> extraSmall
            .copy(bottomEnd = CornerSize(0.dp), bottomStart = CornerSize(0.dp))
        ShapeTokens.MediumShape -> medium
    }
}

@Composable
@ReadOnlyComposable
fun ShapeTokens.toShape(): Shape {
    return MaterialTheme.shapes.fromToken(this)
}
