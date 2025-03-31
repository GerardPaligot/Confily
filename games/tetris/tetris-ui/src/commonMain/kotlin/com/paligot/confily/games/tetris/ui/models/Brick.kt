package com.paligot.confily.games.tetris.ui.models

import androidx.compose.ui.unit.IntOffset

data class Brick(val location: IntOffset = IntOffset(x = 0, y = 0)) {
    fun offsetBy(step: IntOffset) = copy(
        location = IntOffset(x = location.x + step.x, y = location.y + step.y)
    )

    companion object {
        fun of(spirit: Spirit): List<Brick> = spirit.location.map { Brick(it) }
    }
}
