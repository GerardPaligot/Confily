package com.paligot.confily.games.tetris.presentation

import androidx.compose.ui.unit.IntOffset
import com.paligot.confily.games.tetris.ui.models.Brick
import com.paligot.confily.games.tetris.ui.models.Direction
import com.paligot.confily.games.tetris.ui.models.Matrix
import com.paligot.confily.games.tetris.ui.models.Spirit
import com.paligot.confily.games.tetris.ui.models.generateSpiritReverse
import com.paligot.confily.games.tetris.ui.models.toOffset

sealed interface Action {
    fun run(state: GameUiState): GameUiState

    data object Reset : Action {
        override fun run(state: GameUiState): GameUiState {
            if (state.gameStatus == GameStatus.Onboard || state.gameStatus == GameStatus.GameOver) {
                return GameUiState(gameStatus = GameStatus.Running)
            }
            return state.copy(gameStatus = GameStatus.Onboard)
        }
    }

    data object PlayPause : Action {
        override fun run(state: GameUiState): GameUiState {
            return if (state.isRunning) {
                state.copy(gameStatus = GameStatus.Paused)
            } else {
                state.copy(gameStatus = GameStatus.Running)
            }
        }
    }

    data object Pause : Action {
        override fun run(state: GameUiState): GameUiState {
            if (state.isRunning) {
                return state.copy(gameStatus = GameStatus.Paused)
            }
            return state
        }
    }

    data object Resume : Action {
        override fun run(state: GameUiState): GameUiState {
            if (state.isPaused) {
                return state.copy(gameStatus = GameStatus.Running)
            }
            return state
        }
    }

    data class Move(val direction: Direction) : Action {
        override fun run(state: GameUiState): GameUiState {
            if (!state.isRunning) {
                return state
            }
            val spirit = state.spirit.moveBy(direction.toOffset())
            if (spirit.isValidInMatrix(state.bricks, state.matrix)) {
                return state.copy(spirit = spirit)
            }
            return state
        }
    }

    data object Rotate : Action {
        override fun run(state: GameUiState): GameUiState {
            if (!state.isRunning) {
                return state
            }
            val spirit = state.spirit.rotate().adjustOffset(state.matrix)
            if (spirit.isValidInMatrix(state.bricks, state.matrix)) {
                return state.copy(spirit = spirit)
            }
            return state
        }
    }

    data object Drop : Action {
        override fun run(state: GameUiState): GameUiState {
            if (!state.isRunning) {
                return state
            }
            var i = 0
            while (
                state.spirit.moveBy(IntOffset(0, ++i))
                    .isValidInMatrix(state.bricks, state.matrix)
            ) {
                // nothing to do
            }
            val spirit = state.spirit.moveBy(IntOffset(0, i - 1))
            return state.copy(spirit = spirit)
        }
    }

    data object GameTick : Action {
        override fun run(state: GameUiState): GameUiState {
            if (!state.isRunning) {
                return state
            }

            // Spirit continue falling
            if (state.spirit != Spirit.Empty) {
                val spirit = state.spirit.moveBy(Direction.Down.toOffset())
                if (spirit.isValidInMatrix(state.bricks, state.matrix)) {
                    return state.copy(spirit = spirit)
                }
            }

            // GameOver
            if (!state.spirit.isValidInMatrix(state.bricks, state.matrix)) {
                return state.copy(gameStatus = GameStatus.GameOver)
            }

            // Next Spirit
            val newBricks = state.bricks + Brick.of(state.spirit)
            val clearedLines = getClearLines(newBricks, matrix = state.matrix)
            val newState = state.copy(
                spirit = state.spiritNext,
                spiritReserve = (state.spiritReserve - state.spiritNext)
                    .takeIf { it.isNotEmpty() }
                    ?: generateSpiritReverse(state.matrix),
                score = state.score.compute(state.spirit, clearedLines.size)
            )
            return if (clearedLines.isNotEmpty()) {
                newState.copy(
                    bricks = getClearedBricks(newBricks, clearedLines),
                    line = newState.line + clearedLines.size
                )
            } else {
                newState.copy(bricks = newBricks)
            }
        }

        private fun getClearLines(bricks: List<Brick>, matrix: Matrix): List<Int> {
            val map = mutableMapOf<Int, MutableSet<Int>>()
            bricks.forEach {
                map.getOrPut(it.location.y) { mutableSetOf() }.add(it.location.x)
            }
            return map.entries.sortedBy { it.key }
                .filter { it.value.size == matrix.width }
                .map { it.key }
        }

        private fun getClearedBricks(bricks: List<Brick>, clearLines: List<Int>): List<Brick> =
            clearLines.fold(bricks) { acc, line ->
                acc.filter { it.location.y != line }
                    .map { if (it.location.y < line) it.offsetBy(IntOffset(x = 0, y = 1)) else it }
            }
    }
}
