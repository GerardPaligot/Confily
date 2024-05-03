package org.gdglille.devfest.android.theme.m3.style.speakers.avatars

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.android.theme.m3.style.speakers.avatar.BorderedSpeakerAvatarMediumTokens
import org.gdglille.devfest.android.theme.m3.style.speakers.avatar.BorderedSpeakerAvatarSmallTokens
import org.gdglille.devfest.android.theme.m3.style.speakers.avatar.MediumBorderedSpeakerAvatar
import org.gdglille.devfest.android.theme.m3.style.speakers.avatar.SmallBorderedSpeakerAvatar

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SmallBorderedSpeakersAvatar(
    urls: ImmutableList<String>,
    descriptions: ImmutableList<String>?,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    modifier: Modifier = Modifier
) {
    if (descriptions != null) {
        require(urls.size == descriptions.size) {
            "Urls and descriptions arrays should have the exact same size."
        }
    }
    val height = BorderedSpeakerAvatarSmallTokens.ContainerHeight.value
    val spacingPx = with(LocalDensity.current) { 4.dp.toPx() }
    Box(modifier = modifier, contentAlignment = Alignment.CenterEnd) {
        urls.forEachIndexed { index, url ->
            val endPadding = (index * (height - spacingPx)).coerceAtLeast(0f).dp
            val modifierPadding = if (index == 0) Modifier else Modifier.padding(end = endPadding)
            Box(modifier = modifierPadding) {
                SmallBorderedSpeakerAvatar(
                    url = url,
                    contentDescription = descriptions?.getOrNull(index),
                    sharedTransitionScope = sharedTransitionScope,
                    animatedContentScope = animatedContentScope
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MediumBorderedSpeakersAvatar(
    urls: ImmutableList<String>,
    descriptions: ImmutableList<String>?,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    modifier: Modifier = Modifier
) {
    if (descriptions != null) {
        require(urls.size == descriptions.size) {
            "Urls and descriptions arrays should have the exact same size."
        }
    }
    val height = BorderedSpeakerAvatarMediumTokens.ContainerHeight.value
    val spacingPx = with(LocalDensity.current) { 4.dp.toPx() }
    Box(modifier = modifier, contentAlignment = Alignment.CenterEnd) {
        urls.forEachIndexed { index, url ->
            val endPadding = (index * (height - spacingPx)).coerceAtLeast(0f).dp
            val modifierPadding = if (index == 0) Modifier else Modifier.padding(end = endPadding)
            Box(modifier = modifierPadding) {
                MediumBorderedSpeakerAvatar(
                    url = url,
                    contentDescription = descriptions?.getOrNull(index),
                    sharedTransitionScope = sharedTransitionScope,
                    animatedContentScope = animatedContentScope
                )
            }
        }
    }
}

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Composable
private fun SmallBorderedSpeakersAvatarPreview() {
    Conferences4HallTheme {
        SharedTransitionLayout {
            AnimatedContent(targetState = "", label = "") {
                SmallBorderedSpeakersAvatar(
                    urls = persistentListOf("", ""),
                    descriptions = persistentListOf("", ""),
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedContentScope = this@AnimatedContent
                )
            }
        }
    }
}

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Composable
private fun MediumBorderedSpeakersAvatarPreview() {
    Conferences4HallTheme {
        SharedTransitionLayout {
            AnimatedContent(targetState = "", label = "") {
                MediumBorderedSpeakersAvatar(
                    urls = persistentListOf("", ""),
                    descriptions = persistentListOf("", ""),
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedContentScope = this@AnimatedContent
                )
            }
        }
    }
}
