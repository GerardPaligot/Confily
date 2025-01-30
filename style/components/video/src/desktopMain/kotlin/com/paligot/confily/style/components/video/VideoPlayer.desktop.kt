package com.paligot.confily.style.components.video

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
actual fun VideoPlayer(
    url: String,
    modifier: Modifier,
    containerColor: Color,
    useController: Boolean,
    playWhenReady: Boolean
) {
    Text(text = "VideoPlayer not implemented for desktop target")
}
