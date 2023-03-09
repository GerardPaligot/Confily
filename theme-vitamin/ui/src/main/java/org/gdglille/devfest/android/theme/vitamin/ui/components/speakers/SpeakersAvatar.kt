package org.gdglille.devfest.android.theme.vitamin.ui.components.speakers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import org.gdglille.devfest.android.theme.vitamin.ui.theme.Conferences4HallTheme
import org.gdglille.devfest.models.TalkItemUi

private const val SpeakersPadding = 10

@Composable
fun SpeakersAvatar(
    speakersName: ImmutableList<String>,
    speakersAvatar: ImmutableList<String>,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(modifier = modifier, contentAlignment = Alignment.CenterEnd) {
        val height = maxHeight.value
        speakersAvatar.forEachIndexed { index, url ->
            val endPadding = (index * (height - SpeakersPadding)).dp
            val modifierPadding = if (index == 0) Modifier else Modifier.padding(end = endPadding)
            Box(modifier = modifierPadding) {
                SpeakerAvatarBordered(
                    url = url,
                    contentDescription = speakersName[index],
                    modifier = Modifier.size(height.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun SpeakersAvatarPreview() {
    Conferences4HallTheme {
        SpeakersAvatar(
            speakersName = TalkItemUi.fake.speakers,
            speakersAvatar = TalkItemUi.fake.speakersAvatar,
            modifier = Modifier.height(40.dp)
        )
    }
}
