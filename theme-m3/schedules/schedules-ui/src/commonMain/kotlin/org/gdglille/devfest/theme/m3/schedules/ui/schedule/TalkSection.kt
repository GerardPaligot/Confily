package org.gdglille.devfest.theme.m3.schedules.ui.schedule

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
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.paligot.confily.models.ui.TalkUi
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.semantic_talk_item
import com.paligot.confily.resources.semantic_talk_item_level
import com.paligot.confily.resources.text_schedule_minutes
import com.paligot.confily.style.schedules.findCategoryImageVector
import com.paligot.confily.style.schedules.findTimeImageVector
import com.paligot.confily.style.speakers.avatars.MediumBorderedSpeakersAvatar
import com.paligot.confily.style.theme.tags.MediumAutoColoredTag
import com.paligot.confily.style.theme.tags.MediumTag
import com.paligot.confily.style.theme.tags.TagDefaults
import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.compose.resources.stringResource

@Composable
fun TalkSection(
    talk: TalkUi,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground,
    titleTextStyle: TextStyle = MaterialTheme.typography.headlineMedium
) {
    val semanticLevel = if (talk.level == null) {
        ""
    } else {
        stringResource(Resource.string.semantic_talk_item_level, talk.level!!)
    }
    val semanticTalk = stringResource(
        Resource.string.semantic_talk_item,
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
            MediumAutoColoredTag(
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
                descriptions = talk.speakers.map { it.name }.toImmutableList()
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
                text = stringResource(
                    Resource.string.text_schedule_minutes,
                    talk.timeInMinutes.toString()
                ),
                icon = talk.timeInMinutes.findTimeImageVector(),
                colors = TagDefaults.unStyledColors()
            )
        }
    }
}
