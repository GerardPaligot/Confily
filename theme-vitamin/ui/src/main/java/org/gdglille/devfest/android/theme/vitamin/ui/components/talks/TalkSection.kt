package org.gdglille.devfest.android.theme.vitamin.ui.components.talks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decathlon.vitamin.compose.foundation.VitaminTheme
import com.decathlon.vitamin.compose.tags.VitaminTags
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.RichText
import com.halilibo.richtext.ui.RichTextThemeIntegration
import org.gdglille.devfest.android.theme.vitamin.ui.R
import org.gdglille.devfest.android.theme.vitamin.ui.components.speakers.SpeakersAvatar
import org.gdglille.devfest.android.theme.vitamin.ui.theme.Conferences4HallTheme
import org.gdglille.devfest.models.TalkUi

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
        Row(verticalAlignment = Alignment.CenterVertically) {
            VitaminTags.DecorativeGravel(label = talk.room)
            Spacer(modifier = Modifier.weight(1f))
            SpeakersAvatar(
                speakersName = talk.speakers.map { it.name },
                speakersAvatar = talk.speakers.map { it.url },
                modifier = Modifier.height(40.dp)
            )
        }
        Text(
            text = talk.title,
            color = color,
            style = VitaminTheme.typography.h4
        )
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = stringResource(id = R.string.screen_schedule_detail),
                color = color,
                style = VitaminTheme.typography.h6
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                talk.level?.let {
                    val text = when (it) {
                        "advanced" -> stringResource(R.string.text_level_advanced)
                        "intermediate" -> stringResource(R.string.text_level_intermediate)
                        "beginner" -> stringResource(R.string.text_level_beginner)
                        else -> it
                    }
                    VitaminTags.DecorativeCobalt(
                        label = text,
                        iconPainter = painterResource(R.drawable.ic_vtmn_star_line)
                    )
                }
                VitaminTags.DecorativeCobalt(
                    label = stringResource(R.string.text_schedule_minutes, talk.timeInMinutes),
                    iconPainter = painterResource(R.drawable.ic_vtmn_time_line)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            RichTextThemeIntegration(
                textStyle = { VitaminTheme.typography.body3 },
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
