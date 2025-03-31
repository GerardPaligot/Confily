package com.paligot.confily.games.tetris.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.unit.IntOffset
import com.paligot.confily.games.tetris.ui.models.Brick
import com.paligot.confily.games.tetris.ui.models.Matrix
import com.paligot.confily.games.tetris.ui.models.Spirit

val BrickSpirit = Color(0xDD000000)
val BrickMatrix = Color(0x1F000000)

fun DrawScope.drawMatrix(brickSize: Float, matrix: Matrix) {
    (0 until matrix.width).forEach { x ->
        (0 until matrix.height).forEach { y ->
            drawBrick(brickSize, IntOffset(x, y), BrickMatrix)
        }
    }
}

fun DrawScope.drawBricks(brick: List<Brick>, brickSize: Float, matrix: Matrix) {
    clipRect(
        left = 0f,
        top = 0f,
        right = matrix.width * brickSize,
        bottom = matrix.height * brickSize
    ) {
        brick.forEach {
            drawBrick(brickSize, it.location, BrickSpirit)
        }
    }
}

fun DrawScope.drawSpirit(spirit: Spirit, brickSize: Float, matrix: Matrix) {
    clipRect(
        left = 0f,
        top = 0f,
        right = matrix.width * brickSize,
        bottom = matrix.height * brickSize
    ) {
        spirit.location.forEach {
            drawBrick(
                brickSize,
                IntOffset(it.x, it.y),
                BrickSpirit
            )
        }
    }
}

fun DrawScope.drawBrick(
    brickSize: Float,
    offset: IntOffset,
    color: Color
) {
    val actualLocation = Offset(x = offset.x * brickSize, y = offset.y * brickSize)

    val outerSize = brickSize * 0.8f
    val outerOffset = (brickSize - outerSize) / 2

    drawRect(
        color = color,
        topLeft = actualLocation + Offset(outerOffset, outerOffset),
        size = Size(outerSize, outerSize),
        style = Stroke(outerSize / 10)
    )

    val innerSize = brickSize * 0.5f
    val innerOffset = (brickSize - innerSize) / 2

    drawRect(
        color = color,
        topLeft = actualLocation + Offset(innerOffset, innerOffset),
        size = Size(innerSize, innerSize)
    )
}
