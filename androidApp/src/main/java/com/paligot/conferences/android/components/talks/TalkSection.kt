package com.paligot.conferences.android.components.talks

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.paligot.conferences.android.components.speakers.speakerItem
import com.paligot.conferences.android.theme.Conferences4HallTheme
import com.paligot.conferences.repositories.TalkUi

val talk = TalkUi(
    title = "L’intelligence artificielle au secours de l’accessibilité ",
    date = "12:00",
    room = "Stage 1",
    level = "Beginner",
    abstract = "Votre logiciel hang, vous ne savez pas pourquoi ? Ou votre application préférée ne\\nlit pas sa configuration et vous ne savez pas pourquoi ?\\n\\nIl existe beaucoup d'outils fournis avec Linux. Pourtant beaucoup de développeurs\\nne les connaissent pas ou ne les utilisent pas.\\n\\nA travers une série de cas d'utilisation, nous verrons comment utiliser tout ces\\noutils: grep, find, xargs, strace, tcpdump, lsof",
    speakers = arrayListOf(speakerItem, speakerItem)
)

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
        Text(text = talk.date, color = color, style = bodyTextStyle)
        Room(room = talk.room, color = color, style = bodyTextStyle)
        talk.level?.let {
            Text(text = it, color = color, style = bodyTextStyle)
        }
        Spacer(modifier = Modifier.height(8.dp))
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = talk.abstract,
                color = color.copy(LocalContentAlpha.current),
                style = bodyTextStyle
            )
        }
    }
}

@Preview
@Composable
fun TalkSectionPreview() {
    Conferences4HallTheme {
        TalkSection(talk = talk)
    }
}
