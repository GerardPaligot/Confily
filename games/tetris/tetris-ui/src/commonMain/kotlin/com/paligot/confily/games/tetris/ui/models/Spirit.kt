package com.paligot.confily.games.tetris.ui.models

import androidx.compose.ui.unit.IntOffset
import kotlin.math.absoluteValue

data class Spirit(
    val shape: List<IntOffset> = emptyList(),
    val offset: IntOffset = IntOffset(0, 0)
) {
    val location: List<IntOffset> = shape.map { it + offset }

    fun moveBy(step: IntOffset): Spirit = copy(offset = offset + step)

    fun rotate(): Spirit {
        val newShape = shape.toMutableList()
        for (i in shape.indices) {
            newShape[i] = IntOffset(shape[i].y, -shape[i].x)
        }
        return copy(shape = newShape)
    }

    fun isValidInMatrix(blocks: List<Brick>, matrix: Matrix): Boolean {
        return location.none { location ->
            location.x < 0 || location.x > matrix.width - 1 || location.y > matrix.height - 1 ||
                blocks.any { it.location.x == location.x && it.location.y == location.y }
        }
    }

    fun adjustOffset(matrix: Matrix, adjustY: Boolean = true): Spirit {
        val yOffset = if (adjustY) {
            val minY = location.minByOrNull { it.y }
                ?.y
                ?.takeIf { it < 0 }
                ?.absoluteValue ?: 0
            val maxY = location.maxByOrNull { it.y }
                ?.y
                ?.takeIf { it > matrix.height - 1 }
                ?.let { matrix.height - it - 1 }
                ?: 0
            minY + maxY
        } else {
            0
        }
        val minX = location.minByOrNull { it.x }
            ?.x
            ?.takeIf { it < 0 }
            ?.absoluteValue ?: 0
        val maxX = location.maxByOrNull { it.x }
            ?.x
            ?.takeIf { it > matrix.width - 1 }
            ?.let { matrix.width - it - 1 }
            ?: 0
        val xOffset = minX + maxX
        return moveBy(IntOffset(xOffset, yOffset))
    }

    companion object {
        val Empty = Spirit()
    }
}

val SpiritType = listOf(
    listOf(IntOffset(1, -1), IntOffset(1, 0), IntOffset(0, 0), IntOffset(0, 1)), // Z
    listOf(IntOffset(0, -1), IntOffset(0, 0), IntOffset(1, 0), IntOffset(1, 1)), // S
    listOf(IntOffset(0, -1), IntOffset(0, 0), IntOffset(0, 1), IntOffset(0, 2)), // I
    listOf(IntOffset(0, 1), IntOffset(0, 0), IntOffset(0, -1), IntOffset(1, 0)), // T
    listOf(IntOffset(1, 0), IntOffset(0, 0), IntOffset(1, -1), IntOffset(0, -1)), // O
    listOf(IntOffset(0, -1), IntOffset(1, -1), IntOffset(1, 0), IntOffset(1, 1)), // L
    listOf(IntOffset(1, -1), IntOffset(0, -1), IntOffset(0, 0), IntOffset(0, 1)) // J
)

fun generateSpiritReverse(matrix: Matrix): List<Spirit> = SpiritType
    .map {
        Spirit(it, IntOffset((matrix.width - 1) / 2, -1))
            .adjustOffset(matrix, false)
    }
    .shuffled()
