package com.paligot.confily.speakers.panes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.paligot.confily.style.components.placeholder.placeholder
import com.paligot.confily.style.speakers.avatar.MediumSpeakerAvatar
import com.paligot.confily.style.theme.ConfilyTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SpeakerAvatarContent(
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

@Preview
@Composable
private fun SpeakerAvatarScreenPreview() {
    ConfilyTheme {
        SpeakerAvatarContent(url = "")
    }
}
