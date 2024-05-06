package org.gdglille.devfest.android.theme.m3.style.speakers.avatar

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.imageLoader
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MediumSpeakerAvatar(
    url: String,
    contentDescription: String?,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    modifier: Modifier = Modifier,
    placeholder: Painter = SpeakerAvatarDefaults.placeholder,
    contentScale: ContentScale = ContentScale.Crop,
    shape: Shape = SpeakerAvatarDefaults.mediumShape
) {
    SpeakerAvatar(
        url = url,
        placeholder = placeholder,
        contentDescription = contentDescription,
        sharedTransitionScope = sharedTransitionScope,
        animatedContentScope = animatedContentScope,
        modifier = modifier.size(
            width = SpeakerAvatarMediumTokens.ContainerWidth,
            height = SpeakerAvatarMediumTokens.ContainerHeight
        ),
        contentScale = contentScale,
        shape = shape
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun LargeSpeakerAvatar(
    url: String,
    contentDescription: String?,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    modifier: Modifier = Modifier,
    placeholder: Painter = SpeakerAvatarDefaults.placeholder,
    contentScale: ContentScale = ContentScale.Crop,
    shape: Shape = SpeakerAvatarDefaults.largeShape
) {
    SpeakerAvatar(
        url = url,
        placeholder = placeholder,
        contentDescription = contentDescription,
        sharedTransitionScope = sharedTransitionScope,
        animatedContentScope = animatedContentScope,
        modifier = modifier,
        contentScale = contentScale,
        shape = shape
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
internal fun SpeakerAvatar(
    url: String,
    contentDescription: String?,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    placeholder: Painter = SpeakerAvatarDefaults.placeholder,
    border: BorderStroke? = null,
    shape: Shape = SpeakerAvatarDefaults.mediumShape
) {
    with(sharedTransitionScope) {
        Image(
            painter = rememberAsyncImagePainter(
                model = url,
                placeholder = placeholder,
                imageLoader = LocalContext.current.imageLoader
            ),
            contentDescription = contentDescription,
            modifier = modifier
                .then(if (border != null) Modifier.border(border, shape) else Modifier)
                .sharedElement(
                    state = sharedTransitionScope.rememberSharedContentState(key = "avatar-$url"),
                    animatedVisibilityScope = animatedContentScope
                )
                .clip(shape),
            contentScale = contentScale
        )
    }
}

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Composable
private fun MediumSpeakerAvatarPreview() {
    Conferences4HallTheme {
        SharedTransitionLayout {
            AnimatedContent(targetState = "", label = "") {
                Column {
                    MediumSpeakerAvatar(
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
private fun LargeSpeakerAvatarPreview() {
    Conferences4HallTheme {
        SharedTransitionLayout {
            AnimatedContent(targetState = "", label = "") {
                Column {
                    LargeSpeakerAvatar(
                        url = "",
                        contentDescription = null,
                        modifier = Modifier
                            .size(200.dp)
                            .aspectRatio(1f),
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedContentScope = this@AnimatedContent
                    )
                }
            }
        }
    }
}
