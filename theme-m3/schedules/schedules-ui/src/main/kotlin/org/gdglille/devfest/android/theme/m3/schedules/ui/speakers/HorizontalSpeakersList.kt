package org.gdglille.devfest.android.theme.m3.schedules.ui.speakers

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.android.theme.m3.style.R
import org.gdglille.devfest.android.theme.m3.style.speakers.avatars.SmallLabeledSpeakersAvatar
import org.gdglille.devfest.models.ui.TalkItemUi

private const val MaxSpeakersCount = 3

@Composable
fun HorizontalSpeakersList(
    names: ImmutableList<String>,
    avatars: ImmutableList<String>,
    modifier: Modifier = Modifier,
) {
    val count = (names.size - MaxSpeakersCount).coerceAtLeast(minimumValue = 0)
    val speakers = names.take(MaxSpeakersCount).joinToString(", ")
    SmallLabeledSpeakersAvatar(
        label = if (count == 0) speakers else pluralStringResource(
            id = R.plurals.text_speakers_list,
            count = count,
            speakers,
            count
        ),
        urls = avatars.take(MaxSpeakersCount).toImmutableList(),
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun HorizontalSpeakersListPreview() {
    Conferences4HallTheme {
        HorizontalSpeakersList(
            names = TalkItemUi.fake.speakers,
            avatars = TalkItemUi.fake.speakersAvatar
        )
    }
}
