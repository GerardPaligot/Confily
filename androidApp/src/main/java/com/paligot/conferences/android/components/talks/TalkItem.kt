package com.paligot.conferences.android.components.talks

import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.paligot.conferences.android.theme.Conferences4HallTheme
import com.paligot.conferences.repositories.TalkItemUi

val talkItem = TalkItemUi(
    id = "7TTU2GNH3fOu76Q5MrxNkV3ry7l1",
    time = "10:00",
    room = "Salle 700",
    title = "L’intelligence artificielle au secours de l’accessibilité",
    speakers = arrayListOf("Guillaume Laforge", "Aurélie Vache")
)

@Composable
fun TalkBox(
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    indication: Indication = rememberRipple(),
    onClick: () -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier.clickable(
            interactionSource = interactionSource,
            indication = indication,
            onClick = onClick,
            role = Role.Button
        ),
        content = content
    )
}

@Composable
fun TalkItem(
    talk: TalkItemUi,
    modifier: Modifier = Modifier,
    titleColor: Color = MaterialTheme.colors.onBackground,
    titleTextStyle: TextStyle = MaterialTheme.typography.body1,
    roomColor: Color = MaterialTheme.colors.onBackground,
    roomTextStyle: TextStyle = MaterialTheme.typography.caption,
    subtitleColor: Color = MaterialTheme.colors.onBackground,
    subtitleTextStyle: TextStyle = MaterialTheme.typography.body2,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = talk.title,
            color = titleColor,
            style = titleTextStyle,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp)
        )
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            val roomColored = roomColor.copy(alpha = LocalContentAlpha.current)
            Room(
                room = talk.room,
                modifier = Modifier.fillMaxWidth(),
                color = roomColored,
                style = roomTextStyle
            )
            val speakerColored = subtitleColor.copy(alpha = LocalContentAlpha.current)
            Text(
                text = talk.speakers.joinToString(", ") { it },
                style = subtitleTextStyle,
                color = speakerColored,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
fun TalkItemPreview() {
    Conferences4HallTheme {
        TalkBox(onClick = {}) {
            TalkItem(talk = talkItem)
        }
    }
}
