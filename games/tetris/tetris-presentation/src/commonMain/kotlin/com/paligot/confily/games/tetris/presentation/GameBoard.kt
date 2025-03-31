package com.paligot.confily.games.tetris.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.paligot.confily.games.tetris.ui.drawBricks
import com.paligot.confily.games.tetris.ui.drawMatrix
import com.paligot.confily.games.tetris.ui.drawSpirit
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.math.min

@Composable
fun GameBoard(
    viewState: GameUiState,
    onGameTick: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(key1 = Unit) {
        while (isActive) {
            delay(650L - 55 * (viewState.level - 1))
            onGameTick()
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = Unit) {
        val observer = object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                onResume()
            }

            override fun onPause(owner: LifecycleOwner) {
                onPause()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val brickSize = min(
            a = this.constraints.maxWidth.toFloat() / viewState.matrix.width,
            b = this.constraints.maxHeight.toFloat() / viewState.matrix.height
        )
        val height = LocalDensity.current.run { (brickSize * viewState.matrix.height).toDp() }
        val width = LocalDensity.current.run { (brickSize * viewState.matrix.width).toDp() }
        Canvas(
            modifier = Modifier.height(height).width(width),
            onDraw = {
                drawMatrix(brickSize, viewState.matrix)
                drawBricks(viewState.bricks, brickSize, viewState.matrix)
                drawSpirit(viewState.spirit, brickSize, viewState.matrix)
            }
        )
    }
}
