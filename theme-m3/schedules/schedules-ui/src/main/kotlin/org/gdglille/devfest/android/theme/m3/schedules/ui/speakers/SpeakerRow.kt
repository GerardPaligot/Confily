package org.gdglille.devfest.android.theme.m3.schedules.ui.speakers

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
import org.gdglille.devfest.theme.m3.style.speakers.items.LargeSpeakerItem
import org.gdglille.devfest.android.theme.m3.style.toDp
import org.gdglille.devfest.models.ui.SpeakerItemUi

@Composable
fun SpeakerItemRow(
    speakers: ImmutableList<SpeakerItemUi>,
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
                    modifier = Modifier
                        .width(width)
                        .placeholder(isLoading),
                    onClick = { onSpeakerItemClick(it) }
                )
            }
        }
    }
}

@Preview
@Composable
private fun SpeakerItemRowPreview() {
    Conferences4HallTheme {
        SpeakerItemRow(
            speakers = persistentListOf(SpeakerItemUi.fake, SpeakerItemUi.fake),
            onSpeakerItemClick = {}
        )
    }
}
