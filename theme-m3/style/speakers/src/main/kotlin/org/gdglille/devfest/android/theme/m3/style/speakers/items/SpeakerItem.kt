package org.gdglille.devfest.android.theme.m3.style.speakers.items

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.android.theme.m3.style.speakers.avatar.LargeSpeakerAvatar
import org.gdglille.devfest.android.theme.m3.style.toDp

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun LargeSpeakerItem(
    name: String,
    description: String,
    url: String,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    modifier: Modifier = Modifier,
    color: Color = SpeakerItemDefaults.containerColor,
    nameTextStyle: TextStyle = SpeakerItemDefaults.nameTextStyle,
    descriptionTextStyle: TextStyle = SpeakerItemDefaults.descriptionTextStyle,
    shape: Shape = SpeakerItemDefaults.containerShape,
    onClick: () -> Unit
) {
    Card(modifier = modifier, shape = shape, onClick = onClick) {
        Column {
            LargeSpeakerAvatar(
                url = url,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = animatedContentScope
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(SpeakerItemLargeTokens.TextPadding.toDp()),
                verticalArrangement = Arrangement.spacedBy(SpeakerItemLargeTokens.TextBetweenSpacing.toDp())
            ) {
                Text(
                    text = name,
                    color = color,
                    style = nameTextStyle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = description,
                    color = color,
                    style = descriptionTextStyle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Composable
private fun SpeakerItemPreview() {
    Conferences4HallTheme {
        SharedTransitionScope {
            AnimatedContent(targetState = "", label = "") {
                LargeSpeakerItem(
                    name = "Gérard Paligot",
                    description = "Senior Staff Engineer @Decathlon DIgital",
                    url = "",
                    sharedTransitionScope = this@SharedTransitionScope,
                    animatedContentScope = this@AnimatedContent,
                    onClick = {},
                )
            }
        }
    }
}
