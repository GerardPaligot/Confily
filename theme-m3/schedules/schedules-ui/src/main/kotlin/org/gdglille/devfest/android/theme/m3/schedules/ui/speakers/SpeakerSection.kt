package org.gdglille.devfest.android.theme.m3.schedules.ui.speakers

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import org.gdglille.devfest.android.shared.resources.Resource
import org.gdglille.devfest.android.shared.resources.text_speaker_title
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.models.ui.SpeakerItemUi
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalResourceApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SpeakerSection(
    speakers: ImmutableList<SpeakerItemUi>,
    onSpeakerItemClick: (String) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    modifier: Modifier = Modifier,
    subtitleTextStyle: TextStyle = MaterialTheme.typography.titleLarge
) {
    val speakersChunked = remember(speakers) {
        speakers.chunked(size = 2).map { it.toImmutableList() }
    }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(Resource.string.text_speaker_title),
            style = subtitleTextStyle
        )
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            speakersChunked.forEach {
                SpeakerItemRow(
                    speakers = it,
                    onSpeakerItemClick = { onSpeakerItemClick(it.id) },
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
private fun SpeakerSectionPreview() {
    Conferences4HallTheme {
        SharedTransitionLayout {
            AnimatedContent(targetState = "", label = "") {
                SpeakerSection(
                    speakers = persistentListOf(SpeakerItemUi.fake, SpeakerItemUi.fake),
                    onSpeakerItemClick = {},
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedContentScope = this@AnimatedContent
                )
            }
        }
    }
}
