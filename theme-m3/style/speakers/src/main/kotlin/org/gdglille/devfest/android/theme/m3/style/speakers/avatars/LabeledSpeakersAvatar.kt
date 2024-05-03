package org.gdglille.devfest.android.theme.m3.style.speakers.avatars

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.android.theme.m3.style.toDp

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SmallLabeledSpeakersAvatar(
    label: String,
    urls: ImmutableList<String>,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    modifier: Modifier = Modifier,
    color: Color = LabeledSpeakersAvatarDefaults.contentColor,
    style: TextStyle = LabeledSpeakersAvatarDefaults.smallTextStyle
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(LabeledSpeakersAvatarSmallTokens.SpacingBetween.toDp()),
        modifier = modifier
    ) {
        SmallBorderedSpeakersAvatar(
            urls = urls,
            descriptions = null,
            sharedTransitionScope = sharedTransitionScope,
            animatedContentScope = animatedContentScope
        )
        Text(text = label, style = style, color = color)
    }
}

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Composable
private fun HorizontalSpeakersListPreview() {
    Conferences4HallTheme {
        SharedTransitionLayout {
            AnimatedContent(targetState = "", label = "") {
                SmallLabeledSpeakersAvatar(
                    label = "John Doe and Janne Doe",
                    urls = persistentListOf("", ""),
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedContentScope = this@AnimatedContent
                )
            }
        }
    }
}
