package org.gdglille.devfest.android.theme.m3.style.speakers.avatar

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SmallBorderedSpeakerAvatar(
    url: String,
    contentDescription: String?,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    modifier: Modifier = Modifier,
    border: BorderStroke = SpeakerAvatarDefaults.borderedSmall,
    contentScale: ContentScale = ContentScale.Crop,
    shape: Shape = SpeakerAvatarDefaults.borderedSmallShape
) {
    SpeakerAvatar(
        url = url,
        contentDescription = contentDescription,
        sharedTransitionScope = sharedTransitionScope,
        animatedContentScope = animatedContentScope,
        modifier = modifier.size(
            width = BorderedSpeakerAvatarSmallTokens.ContainerWidth,
            height = BorderedSpeakerAvatarSmallTokens.ContainerHeight
        ),
        contentScale = contentScale,
        border = border,
        shape = shape
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MediumBorderedSpeakerAvatar(
    url: String,
    contentDescription: String?,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    modifier: Modifier = Modifier,
    border: BorderStroke = SpeakerAvatarDefaults.borderedMedium,
    contentScale: ContentScale = ContentScale.Crop,
    shape: Shape = SpeakerAvatarDefaults.borderedMediumShape
) {
    SpeakerAvatar(
        url = url,
        contentDescription = contentDescription,
        sharedTransitionScope = sharedTransitionScope,
        animatedContentScope = animatedContentScope,
        modifier = modifier.size(
            width = BorderedSpeakerAvatarMediumTokens.ContainerWidth,
            height = BorderedSpeakerAvatarMediumTokens.ContainerHeight
        ),
        contentScale = contentScale,
        border = border,
        shape = shape
    )
}

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Composable
private fun SmallBorderedSpeakerAvatarPreview() {
    Conferences4HallTheme {
        SharedTransitionLayout {
            AnimatedContent(targetState = "", label = "") {
                Column {
                    SmallBorderedSpeakerAvatar(
                        url = "",
                        contentDescription = null,
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedContentScope = this@AnimatedContent
                    )
                }
            }
        }
    }
}

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Composable
private fun MediumBorderedSpeakerAvatarPreview() {
    Conferences4HallTheme {
        SharedTransitionLayout {
            AnimatedContent(targetState = "", label = "") {
                Column {
                    MediumBorderedSpeakerAvatar(
                        url = "",
                        contentDescription = null,
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedContentScope = this@AnimatedContent
                    )
                }
            }
        }
    }
}
