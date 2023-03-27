package org.gdglille.devfest.android.theme.vitamin.ui.components.talks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decathlon.vitamin.compose.foundation.VitaminTheme
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.RichText
import com.halilibo.richtext.ui.RichTextThemeIntegration
import kotlinx.collections.immutable.toImmutableList
import org.gdglille.devfest.android.theme.vitamin.ui.components.speakers.SpeakersAvatar
import org.gdglille.devfest.android.theme.vitamin.ui.components.tags.DecorativeTag
import org.gdglille.devfest.android.theme.vitamin.ui.components.tags.LevelTag
import org.gdglille.devfest.android.theme.vitamin.ui.components.tags.UnStyledTag
import org.gdglille.devfest.android.theme.vitamin.ui.theme.Conferences4HallTheme
import org.gdglille.devfest.android.ui.resources.R
import org.gdglille.devfest.models.TalkUi
import com.decathlon.vitamin.compose.foundation.R as RVitamin

@Composable
fun TalkSection(
    talk: TalkUi,
    modifier: Modifier = Modifier,
    color: Color = VitaminTheme.colors.vtmnContentPrimary
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
                    modifier = Modifier.height(40.dp)
                )
            }
        }
        Text(
            text = talk.title,
            color = color,
            style = VitaminTheme.typography.h4
        )
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            UnStyledTag(
                text = talk.startTime,
                icon = RVitamin.drawable.ic_vtmn_time_line
            )
            UnStyledTag(
                text = talk.room,
                icon = RVitamin.drawable.ic_vtmn_map_pin_line
            )
            UnStyledTag(
                text = stringResource(R.string.text_schedule_minutes, talk.timeInMinutes.toString()),
                icon = if (talk.timeInMinutes <= ShortTalk) {
                    RVitamin.drawable.ic_vtmn_flashlight_line
                } else {
                    RVitamin.drawable.ic_vtmn_timer_line
                }
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = stringResource(id = R.string.screen_schedule_detail),
                color = color,
                style = VitaminTheme.typography.h6
            )
            RichTextThemeIntegration(
                textStyle = { VitaminTheme.typography.body2 },
                ProvideTextStyle = null,
                contentColor = { color },
                ProvideContentColor = null,
            ) {
                RichText {
                    Markdown(talk.abstract)
                }
            }
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
