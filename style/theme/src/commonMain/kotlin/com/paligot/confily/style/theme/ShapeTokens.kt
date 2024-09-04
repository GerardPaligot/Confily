package com.paligot.confily.style.theme

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
    ExtraSmallShape,
    SmallShape,
    MediumShape
}

internal fun Shapes.fromToken(value: ShapeTokens): Shape {
    return when (value) {
        ShapeTokens.CircleShape -> CircleShape
        ShapeTokens.ExtraSmallTopShape ->
            extraSmall
                .copy(bottomEnd = CornerSize(0.dp), bottomStart = CornerSize(0.dp))
        ShapeTokens.ExtraSmallShape -> extraSmall
        ShapeTokens.SmallShape -> small
        ShapeTokens.MediumShape -> medium
    }
}

@Composable
@ReadOnlyComposable
fun ShapeTokens.toShape(): Shape {
    return MaterialTheme.shapes.fromToken(this)
}
