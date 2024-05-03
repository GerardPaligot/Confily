package org.gdglille.devfest.android.theme.m3.speakers.screens

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.android.theme.m3.style.placeholder.placeholder
import org.gdglille.devfest.android.theme.m3.style.speakers.avatar.MediumSpeakerAvatar

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SpeakerAvatarScreen(
    url: String,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
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
            sharedTransitionScope = sharedTransitionScope,
            animatedContentScope = animatedContentScope,
            modifier = Modifier.placeholder(visible = isLoading)
        )
    }
}

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Composable
private fun SpeakerAvatarScreenPreview() {
    Conferences4HallTheme {
        SharedTransitionLayout {
            AnimatedContent(targetState = "", label = "") {
                SpeakerAvatarScreen(
                    url = "",
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedContentScope = this@AnimatedContent
                )
            }
        }
    }
}
