package com.paligot.confily.infos.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.paligot.confily.games.tetris.presentation.Action
import com.paligot.confily.games.tetris.presentation.GameBoard
import com.paligot.confily.games.tetris.presentation.GameViewModel
import com.paligot.confily.games.tetris.ui.GameController
import com.paligot.confily.games.tetris.ui.ScoringBoard
import com.paligot.confily.games.tetris.ui.models.Direction
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.action_game_refresh
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TetrisGameVM(
    modifier: Modifier = Modifier,
    viewModel: GameViewModel = koinViewModel()
) {
    val viewState = viewModel.state.value
    LaunchedEffect(Unit) {
        viewModel.dispatch(Action.Reset)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Tetris Game") },
                actions = {
                    IconButton(onClick = { viewModel.dispatch(Action.PlayPause) }) {
                        Icon(
                            imageVector = if (viewState.isPaused) Icons.Outlined.PlayArrow else Icons.Outlined.Pause,
                            contentDescription = stringResource(Resource.string.action_game_refresh)
                        )
                    }
                    IconButton(onClick = { viewModel.dispatch(Action.Reset) }) {
                        Icon(
                            imageVector = Icons.Outlined.Refresh,
                            contentDescription = stringResource(Resource.string.action_game_refresh)
                        )
                    }
                }
            )
        }
    ) {
        Column(
            modifier = modifier.padding(it).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ScoringBoard(
                score = viewState.score.value,
                level = viewState.level,
                lines = viewState.line,
                spirit = viewState.spiritNext
            )
            GameBoard(
                viewState = viewState,
                modifier = Modifier.weight(1f),
                onGameTick = { viewModel.dispatch(Action.GameTick) },
                onPause = { viewModel.dispatch(Action.Pause) },
                onResume = { viewModel.dispatch(Action.Resume) }
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
