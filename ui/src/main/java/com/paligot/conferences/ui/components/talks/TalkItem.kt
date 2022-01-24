package com.paligot.conferences.ui.components.talks

import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.paligot.conferences.repositories.TalkItemUi
import com.paligot.conferences.ui.theme.Conferences4HallTheme

val talkItem = TalkItemUi(
    id = "7TTU2GNH3fOu76Q5MrxNkV3ry7l1",
    time = "10:00",
    room = "Salle 700",
    title = "L’intelligence artificielle au secours de l’accessibilité",
    speakers = arrayListOf("Guillaume Laforge", "Aurélie Vache"),
    isFavorite = false
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
    favoriteColor: Color = if (talk.isFavorite) MaterialTheme.colors.primaryVariant else MaterialTheme.colors.onBackground,
    onFavoriteClicked: (id: String, isFavorite: Boolean) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = talk.title,
                color = titleColor,
                style = titleTextStyle,
            )
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                val roomColored = roomColor.copy(alpha = LocalContentAlpha.current)
                Room(
                    room = talk.room,
                    color = roomColored,
                    style = roomTextStyle
                )
                val speakerColored = subtitleColor.copy(alpha = LocalContentAlpha.current)
                Text(
                    text = talk.speakers.joinToString(", ") { it },
                    style = subtitleTextStyle,
                    color = speakerColored,
                )
            }
        }
        IconButton(
            onClick = { onFavoriteClicked(talk.id, !talk.isFavorite) },
            modifier = Modifier.padding(end = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Star,
                contentDescription = "Add talk in favorite",
                tint = favoriteColor
            )
        }
    }
}

@Preview
@Composable
fun TalkItemPreview() {
    Conferences4HallTheme {
        Scaffold {
            TalkBox(onClick = {}) {
                TalkItem(talk = talkItem) { _, _ -> }
            }
        }
    }
}
