package org.gdglille.devfest.android.theme.m3.schedules.ui.speakers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.android.theme.m3.style.R
import org.gdglille.devfest.android.theme.m3.style.speakers.avatars.SmallBorderedSpeakersAvatar
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
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        SmallBorderedSpeakersAvatar(
            urls = avatars.take(MaxSpeakersCount).toImmutableList(),
            descriptions = names.take(MaxSpeakersCount).toImmutableList()
        )
        Text(
            text = if (count == 0) speakers else pluralStringResource(
                id = R.plurals.text_speakers_list,
                count = count,
                speakers,
                count
            ),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HorizontalSpeakersListPreview() {
    Conferences4HallTheme {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            HorizontalSpeakersList(
                names = TalkItemUi.fake.speakers,
                avatars = TalkItemUi.fake.speakersAvatar
            )
        }
    }
}
