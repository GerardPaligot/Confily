package org.gdglille.devfest.android.components.talks

import android.annotation.SuppressLint
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
import androidx.compose.material.icons.outlined.Grade
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.theme.Conferences4HallTheme
import org.gdglille.devfest.android.ui.R
import org.gdglille.devfest.models.TalkItemUi

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
    favoriteColor: Color = if (talk.isFavorite) MaterialTheme.colors.secondary else MaterialTheme.colors.onBackground,
    onFavoriteClicked: (TalkItemUi) -> Unit
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
        if (!talk.isPause) {
            IconButton(
                onClick = { onFavoriteClicked(talk) },
                modifier = Modifier.padding(end = 4.dp)
            ) {
                Icon(
                    imageVector = if (talk.isFavorite) Icons.Outlined.Star else Icons.Outlined.Grade,
                    contentDescription = if (talk.isFavorite) stringResource(R.string.action_favorites_remove)
                    else stringResource(R.string.action_favorites_add),
                    tint = favoriteColor
                )
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Preview
@Composable
fun TalkItemPreview() {
    Conferences4HallTheme {
        Scaffold {
            TalkItem(talk = TalkItemUi.fake) { }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Preview
@Composable
fun TalkItemPausePreview() {
    Conferences4HallTheme {
        Scaffold {
            TalkItem(talk = TalkItemUi.fakePause) { }
        }
    }
}
