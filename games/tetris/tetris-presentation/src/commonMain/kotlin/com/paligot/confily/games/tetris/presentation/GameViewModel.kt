package com.paligot.confily.games.tetris.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paligot.confily.games.tetris.ui.models.Brick
import com.paligot.confily.games.tetris.ui.models.Matrix
import com.paligot.confily.games.tetris.ui.models.Score
import com.paligot.confily.games.tetris.ui.models.Spirit
import kotlinx.coroutines.launch
import kotlin.math.min

private const val MatrixWidth = 12
private const val MatrixHeight = 24

data class GameUiState(
    val bricks: List<Brick> = emptyList(),
    val spirit: Spirit = Spirit.Empty,
    val spiritReserve: List<Spirit> = emptyList(),
    val matrix: Matrix = Matrix(MatrixWidth, MatrixHeight),
    val gameStatus: GameStatus = GameStatus.Onboard,
    val score: Score = Score(),
    val line: Int = 0
) {
    val level: Int
        get() = min(10, 1 + line / 20)

    val spiritNext: Spirit
        get() = spiritReserve.firstOrNull() ?: Spirit.Empty

    val isPaused
        get() = gameStatus == GameStatus.Paused

    val isRunning
        get() = gameStatus == GameStatus.Running
}

class GameViewModel : ViewModel() {
    private val _state: MutableState<GameUiState> = mutableStateOf(GameUiState())
    val state: State<GameUiState> = _state

    fun dispatch(action: Action) = viewModelScope.launch {
        _state.value = action.run(state = _state.value)
    }
}
