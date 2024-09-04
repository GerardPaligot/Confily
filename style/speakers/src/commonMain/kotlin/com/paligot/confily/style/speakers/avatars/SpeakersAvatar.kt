package com.paligot.confily.style.speakers.avatars

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.paligot.confily.style.speakers.avatar.BorderedSpeakerAvatarMediumTokens
import com.paligot.confily.style.speakers.avatar.BorderedSpeakerAvatarSmallTokens
import com.paligot.confily.style.speakers.avatar.MediumBorderedSpeakerAvatar
import com.paligot.confily.style.speakers.avatar.SmallBorderedSpeakerAvatar
import kotlinx.collections.immutable.ImmutableList

@Composable
fun SmallBorderedSpeakersAvatar(
    urls: ImmutableList<String>,
    descriptions: ImmutableList<String>?,
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
                    contentDescription = descriptions?.getOrNull(index)
                )
            }
        }
    }
}

@Composable
fun MediumBorderedSpeakersAvatar(
    urls: ImmutableList<String>,
    descriptions: ImmutableList<String>?,
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
                    contentDescription = descriptions?.getOrNull(index)
                )
            }
        }
    }
}
