// ktlint-disable filename
package com.paligot.confily.games.tetris

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.lifecycle.viewmodel.compose.viewModel
import com.paligot.confily.games.tetris.presentation.Action
import com.paligot.confily.games.tetris.presentation.GameBoard
import com.paligot.confily.games.tetris.presentation.GameViewModel
import com.paligot.confily.games.tetris.ui.GameController
import com.paligot.confily.games.tetris.ui.ScoringBoard
import com.paligot.confily.games.tetris.ui.models.Direction

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Tetris Game"
    ) {
        val requester = remember { FocusRequester() }
        val viewModel = viewModel<GameViewModel>()
        val viewState = viewModel.state.value
        LaunchedEffect(Unit) {
            requester.requestFocus()
        }
        Box(
            modifier = Modifier
                .focusRequester(requester)
                .focusable()
                .onPreviewKeyEvent {
                    if (it.type == KeyEventType.KeyUp) return@onPreviewKeyEvent false
                    when (it.key) {
                        Key.R -> {
                            viewModel.dispatch(Action.Reset)
                            true
                        }
                        Key.P -> {
                            viewModel.dispatch(Action.Pause)
                            true
                        }
                        Key.DirectionLeft -> {
                            viewModel.dispatch(Action.Move(Direction.Left))
                            true
                        }
                        Key.DirectionRight -> {
                            viewModel.dispatch(Action.Move(Direction.Right))
                            true
                        }
                        Key.DirectionUp -> {
                            viewModel.dispatch(Action.Rotate)
                            true
                        }
                        Key.DirectionDown -> {
                            viewModel.dispatch(Action.Move(Direction.Down))
                            true
                        }
                        Key.Spacebar -> {
                            viewModel.dispatch(Action.Drop)
                            true
                        }
                        else -> false
                    }
                }
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GameBoard(
                    viewState = viewState,
                    modifier = Modifier.weight(1f),
                    onGameTick = { viewModel.dispatch(Action.GameTick) },
                    onPause = { viewModel.dispatch(Action.Pause) },
                    onResume = { viewModel.dispatch(Action.Resume) }
                )
                ScoringBoard(
                    score = viewState.score.value,
                    level = viewState.level,
                    lines = viewState.line,
                    spirit = viewState.spiritNext
                )
                GameController(
                    onMoveLeft = { viewModel.dispatch(Action.Move(Direction.Left)) },
                    onMoveRight = { viewModel.dispatch(Action.Move(Direction.Right)) },
                    onRotate = { viewModel.dispatch(Action.Rotate) },
                    onDrop = { viewModel.dispatch(Action.Drop) },
                    modifier = Modifier.height(150.dp).padding(vertical = 16.dp)
                )
            }
        }
    }
}
