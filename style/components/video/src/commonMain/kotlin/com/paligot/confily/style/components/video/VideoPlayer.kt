package com.paligot.confily.style.components.video

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
expect fun VideoPlayer(
    url: String,
    modifier: Modifier = Modifier,
    containerColor: Color = Color.Black,
    useController: Boolean = true,
    playWhenReady: Boolean = true
)