package com.paligot.confily.games.tetris.ui.models

import androidx.compose.ui.unit.IntOffset

enum class Direction {
    Left, Up, Right, Down
}

fun Direction.toOffset() = when (this) {
    Direction.Left -> IntOffset(-1, 0)
    Direction.Up -> IntOffset(0, -1)
    Direction.Right -> IntOffset(1, 0)
    Direction.Down -> IntOffset(0, 1)
}
