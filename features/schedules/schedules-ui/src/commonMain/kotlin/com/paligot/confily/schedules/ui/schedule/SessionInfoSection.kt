package com.paligot.confily.schedules.ui.schedule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Label
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
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.semantic_talk_item
import com.paligot.confily.resources.semantic_talk_item_level
import com.paligot.confily.resources.text_schedule_minutes
import com.paligot.confily.schedules.ui.models.SessionInfoUi
import com.paligot.confily.schedules.ui.models.SpeakerItemUi
import com.paligot.confily.style.schedules.findCategoryImageVector
import com.paligot.confily.style.schedules.findTimeImageVector
import com.paligot.confily.style.speakers.avatars.MediumBorderedSpeakersAvatar
import com.paligot.confily.style.theme.tags.MediumAutoColoredTag
import com.paligot.confily.style.theme.tags.MediumTag
import com.paligot.confily.style.theme.tags.SmallTag
import com.paligot.confily.style.theme.tags.TagDefaults
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SessionInfoSection(
    info: SessionInfoUi,
    speakers: ImmutableList<SpeakerItemUi>,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground,
    titleTextStyle: TextStyle = MaterialTheme.typography.headlineMedium
) {
    val semanticLevel = if (info.level == null) {
        ""
    } else {
        stringResource(Resource.string.semantic_talk_item_level, info.level!!)
    }
    val semanticTalk = stringResource(
        Resource.string.semantic_talk_item,
        info.title,
        "",
        info.room,
        info.timeInMinutes,
        info.category.name,
        semanticLevel
    )
    Column(
        modifier = modifier.clearAndSetSemantics { contentDescription = semanticTalk },
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            MediumAutoColoredTag(
                text = info.category.name,
                colorName = info.category.color,
                icon = info.category.icon?.findCategoryImageVector()
            )
            Spacer(modifier = Modifier.width(8.dp))
            info.level?.let {
                MediumTag(text = it, colors = TagDefaults.gravelColors())
            }
            Spacer(modifier = Modifier.weight(1f))
            MediumBorderedSpeakersAvatar(
                urls = speakers.map { it.photoUrl }.toImmutableList(),
                descriptions = speakers.map { it.displayName }.toImmutableList()
            )
        }
        Text(text = info.title, color = color, style = titleTextStyle)
        Row {
            MediumTag(
                text = info.slotTime,
                icon = Icons.Outlined.Schedule,
                colors = TagDefaults.unStyledColors()
            )
            MediumTag(
                text = info.room,
                icon = Icons.Outlined.Videocam,
                colors = TagDefaults.unStyledColors()
            )
            MediumTag(
                text = stringResource(
                    Resource.string.text_schedule_minutes,
                    info.timeInMinutes.toString()
                ),
                icon = info.timeInMinutes.findTimeImageVector(),
                colors = TagDefaults.unStyledColors()
            )
        }
        if (info.tags.isNotEmpty()) {
            FlowRow {
                info.tags.forEach { tag ->
                    SmallTag(
                        text = tag.name,
                        icon = Icons.AutoMirrored.Outlined.Label,
                        colors = TagDefaults.unStyledColors()
                    )
                }
            }
        }
    }
}
