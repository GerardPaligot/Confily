package org.gdglille.devfest.android.theme.m3.schedules.ui.schedule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.toImmutableList
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.android.theme.m3.style.R
import org.gdglille.devfest.android.theme.m3.style.schedules.findCategoryImageVector
import org.gdglille.devfest.android.theme.m3.style.schedules.findTimeImageVector
import org.gdglille.devfest.android.theme.m3.style.speakers.avatars.MediumBorderedSpeakersAvatar
import org.gdglille.devfest.android.theme.m3.style.tags.AutoColoredTag
import org.gdglille.devfest.android.theme.m3.style.tags.MediumTag
import org.gdglille.devfest.android.theme.m3.style.tags.TagDefaults
import org.gdglille.devfest.models.ui.TalkUi

@Composable
fun TalkSection(
    talk: TalkUi,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground,
    titleTextStyle: TextStyle = MaterialTheme.typography.headlineMedium
) {
    val semanticLevel = if (talk.level == null) ""
    else stringResource(id = R.string.semantic_talk_item_level, talk.level!!)
    val semanticTalk = stringResource(
        id = R.string.semantic_talk_item,
        talk.title,
        "",
        talk.room,
        talk.timeInMinutes,
        talk.category.name,
        semanticLevel
    )
    Column(
        modifier = modifier.clearAndSetSemantics { contentDescription = semanticTalk },
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AutoColoredTag(
                text = talk.category.name,
                colorName = talk.category.color,
                icon = talk.category.icon?.findCategoryImageVector()
            )
            Spacer(modifier = Modifier.width(8.dp))
            talk.level?.let {
                MediumTag(text = it, colors = TagDefaults.gravelColors())
            }
            Spacer(modifier = Modifier.weight(1f))
            MediumBorderedSpeakersAvatar(
                urls = talk.speakers.map { it.url }.toImmutableList(),
                descriptions = talk.speakers.map { it.name }.toImmutableList(),
            )
        }
        Text(text = talk.title, color = color, style = titleTextStyle)
        Row {
            MediumTag(
                text = talk.startTime,
                icon = Icons.Outlined.Schedule,
                colors = TagDefaults.unStyledColors()
            )
            MediumTag(
                text = talk.room,
                icon = Icons.Outlined.Videocam,
                colors = TagDefaults.unStyledColors()
            )
            MediumTag(
                text = stringResource(R.string.text_schedule_minutes, talk.timeInMinutes.toString()),
                icon = talk.timeInMinutes.findTimeImageVector(),
                colors = TagDefaults.unStyledColors()
            )
        }
    }
}

@Preview
@Composable
fun TalkSectionPreview() {
    Conferences4HallTheme {
        TalkSection(talk = TalkUi.fake)
    }
}
