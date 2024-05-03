package org.gdglille.devfest.android.theme.m3.schedules.ui.speakers

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.android.theme.m3.style.SpacingTokens
import org.gdglille.devfest.android.theme.m3.style.placeholder.placeholder
import org.gdglille.devfest.android.theme.m3.style.speakers.items.LargeSpeakerItem
import org.gdglille.devfest.android.theme.m3.style.toDp
import org.gdglille.devfest.models.ui.SpeakerItemUi

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SpeakerItemRow(
    speakers: ImmutableList<SpeakerItemUi>,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    modifier: Modifier = Modifier,
    maxItems: Int = 2,
    isLoading: Boolean = false,
    onSpeakerItemClick: (SpeakerItemUi) -> Unit
) {
    val horizontalSpacing = SpacingTokens.MediumSpacing.toDp()
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val width = (this.maxWidth - (horizontalSpacing * (maxItems - 1))) / maxItems
        Row(horizontalArrangement = Arrangement.spacedBy(horizontalSpacing)) {
            speakers.forEach {
                LargeSpeakerItem(
                    name = it.name,
                    description = it.company,
                    url = it.url,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedContentScope = animatedContentScope,
                    modifier = Modifier
                        .width(width)
                        .placeholder(isLoading),
                    onClick = { onSpeakerItemClick(it) }
                )
            }
        }
    }
}

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Composable
private fun SpeakerItemRowPreview() {
    Conferences4HallTheme {
        SharedTransitionLayout {
            AnimatedContent(targetState = "", label = "") {
                SpeakerItemRow(
                    speakers = persistentListOf(SpeakerItemUi.fake, SpeakerItemUi.fake),
                    onSpeakerItemClick = {},
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedContentScope = this@AnimatedContent
                )
            }
        }
    }
}
