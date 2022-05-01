package org.gdglille.devfest.android.components.talks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.RichText
import com.halilibo.richtext.ui.RichTextThemeIntegration
import org.gdglille.devfest.android.ui.R
import org.gdglille.devfest.android.theme.Conferences4HallTheme
import kotlinx.datetime.toLocalDateTime
import org.gdglille.devfest.extensions.formatHoursMinutes
import org.gdglille.devfest.models.TalkUi

@Composable
fun TalkSection(
    talk: TalkUi,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.onBackground,
    titleTextStyle: TextStyle = MaterialTheme.typography.h6,
    bodyTextStyle: TextStyle = MaterialTheme.typography.body2
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = talk.title, color = color, style = titleTextStyle)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.text_schedule_time, talk.startTime, talk.endTime),
            color = color,
            style = bodyTextStyle
        )
        Room(room = talk.room, color = color, style = bodyTextStyle)
        talk.level?.let {
            val text = when (it) {
                "advanced" -> stringResource(R.string.text_level_advanced)
                "intermediate" -> stringResource(R.string.text_level_intermediate)
                "beginner" -> stringResource(R.string.text_level_beginner)
                else -> it
            }
            Text(text = text, color = color, style = bodyTextStyle)
        }
        Spacer(modifier = Modifier.height(8.dp))
        CompositionLocalProvider(
            LocalContentAlpha provides ContentAlpha.medium
        ) {
            RichTextThemeIntegration(
                textStyle = { bodyTextStyle },
                ProvideTextStyle = null,
                contentColor = { color.copy(LocalContentAlpha.current) },
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
