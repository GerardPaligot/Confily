package com.paligot.confily.games.tetris.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.East
import androidx.compose.material.icons.outlined.SettingsBackupRestore
import androidx.compose.material.icons.outlined.South
import androidx.compose.material.icons.outlined.West
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.action_game_drop
import com.paligot.confily.resources.action_game_left
import com.paligot.confily.resources.action_game_right
import com.paligot.confily.resources.action_game_rotate
import org.jetbrains.compose.resources.stringResource

@Composable
fun GameController(
    onMoveLeft: () -> Unit,
    onMoveRight: () -> Unit,
    onRotate: () -> Unit,
    onDrop: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Surface(
            onClick = onMoveLeft,
            modifier = Modifier.fillMaxHeight().weight(1f)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.West,
                    contentDescription = stringResource(Resource.string.action_game_left)
                )
            }
        }
        Column(
            modifier = Modifier.fillMaxHeight().weight(1f)
        ) {
            Surface(
                onClick = onRotate,
                modifier = Modifier.fillMaxWidth().weight(1f)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.SettingsBackupRestore,
                        contentDescription = stringResource(Resource.string.action_game_rotate)
                    )
                }
            }
            Surface(
                onClick = onDrop,
                modifier = Modifier.fillMaxWidth().weight(1f)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.South,
                        contentDescription = stringResource(Resource.string.action_game_drop)
                    )
                }
            }
        }
        Surface(
            onClick = onMoveRight,
            modifier = Modifier.fillMaxHeight().weight(1f)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.East,
                    contentDescription = stringResource(Resource.string.action_game_right)
                )
            }
        }
    }
}
