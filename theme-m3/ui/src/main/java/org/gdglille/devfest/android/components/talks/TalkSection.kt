package org.gdglille.devfest.android.components.talks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Timer
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
import org.gdglille.devfest.android.components.speakers.SpeakersAvatar
import org.gdglille.devfest.android.components.tags.DecorativeTag
import org.gdglille.devfest.android.components.tags.LevelTag
import org.gdglille.devfest.android.components.tags.Tag
import org.gdglille.devfest.android.components.tags.TagDefaults
import org.gdglille.devfest.android.theme.Conferences4HallTheme
import org.gdglille.devfest.android.ui.resources.R
import org.gdglille.devfest.models.TalkUi

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
            talk.category.color?.let {
                DecorativeTag(category = talk.category)
            }
            Spacer(modifier = Modifier.width(8.dp))
            talk.level?.let {
                LevelTag(level = it)
            }
            Spacer(modifier = Modifier.weight(1f))
            SpeakersAvatar(
                speakersName = talk.speakers.map { it.name }.toImmutableList(),
                speakersAvatar = talk.speakers.map { it.url }.toImmutableList(),
                modifier = Modifier.height(40.dp),
                betweenSpacing = 6.dp
            )
        }
        Text(text = talk.title, color = color, style = titleTextStyle)
        Row {
            Tag(
                text = talk.startTime,
                icon = Icons.Outlined.Schedule,
                colors = TagDefaults.unStyledColors()
            )
            Tag(
                text = talk.room,
                icon = Icons.Outlined.Videocam,
                colors = TagDefaults.unStyledColors()
            )
            Tag(
                text = stringResource(R.string.text_schedule_minutes, talk.timeInMinutes.toString()),
                icon = if (talk.timeInMinutes <= ShortTalk) {
                    Icons.Outlined.Bolt
                } else {
                    Icons.Outlined.Timer
                },
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
