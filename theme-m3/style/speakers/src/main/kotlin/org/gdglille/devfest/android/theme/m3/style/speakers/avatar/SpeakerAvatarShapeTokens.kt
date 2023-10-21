package org.gdglille.devfest.android.theme.m3.style.speakers.avatar

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

internal enum class SpeakerAvatarShapeTokens {
    CircleShape,
    TopRoundedShape
}

internal fun Shapes.fromToken(value: SpeakerAvatarShapeTokens): Shape {
    return when (value) {
        SpeakerAvatarShapeTokens.CircleShape -> CircleShape
        SpeakerAvatarShapeTokens.TopRoundedShape -> extraSmall
            .copy(bottomEnd = CornerSize(0.dp), bottomStart = CornerSize(0.dp))
    }
}

@Composable
@ReadOnlyComposable
internal fun SpeakerAvatarShapeTokens.toShape(): Shape {
    return MaterialTheme.shapes.fromToken(this)
}
