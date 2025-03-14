package com.paligot.confily.speakers.panes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.paligot.confily.style.components.placeholder.placeholder
import com.paligot.confily.style.speakers.avatar.MediumSpeakerAvatar

@Composable
fun SpeakerAvatarScreen(
    url: String,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        MediumSpeakerAvatar(
            url = url,
            contentDescription = null,
            modifier = Modifier.placeholder(visible = isLoading)
        )
    }
}
