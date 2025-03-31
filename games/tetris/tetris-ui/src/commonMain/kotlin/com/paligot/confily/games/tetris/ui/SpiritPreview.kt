package com.paligot.confily.games.tetris.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import com.paligot.confily.games.tetris.ui.models.Matrix
import com.paligot.confily.games.tetris.ui.models.Spirit
import kotlin.math.min

@Composable
fun SpiritPreview(
    spirit: Spirit,
    modifier: Modifier = Modifier
) {
    val matrix = remember { Matrix(width = 4, height = 2) }
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val brickSize = min(
            a = this.constraints.maxWidth.toFloat() / matrix.width,
            b = this.constraints.maxHeight.toFloat() / matrix.height
        )
        val height = LocalDensity.current.run { (brickSize * matrix.height).toDp() }
        val width = LocalDensity.current.run { (brickSize * matrix.width).toDp() }
        Canvas(
            modifier = Modifier.height(height).width(width),
            onDraw = {
                drawMatrix(brickSize, matrix)
                drawSpirit(spirit.rotate().adjustOffset(matrix), brickSize, matrix)
            }
        )
    }
}
