package org.gdglille.devfest.theme.m3.speakers.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.gdglille.devfest.theme.m3.style.placeholder.placeholder
import org.gdglille.devfest.theme.m3.style.speakers.avatar.MediumSpeakerAvatar

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
