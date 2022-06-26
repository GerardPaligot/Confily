package org.gdglille.devfest.android.components.talks

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Grade
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    titleColor: Color = MaterialTheme.colorScheme.onBackground,
    titleTextStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    roomColor: Color = MaterialTheme.colorScheme.onBackground,
    roomTextStyle: TextStyle = MaterialTheme.typography.bodySmall,
    subtitleColor: Color = MaterialTheme.colorScheme.onBackground,
    subtitleTextStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    favoriteColor: Color = if (talk.isFavorite) MaterialTheme.colorScheme.secondary
    else MaterialTheme.colorScheme.onBackground,
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
            val roomColored = roomColor.copy(alpha = .73f)
            Room(
                room = talk.room,
                color = roomColored,
                style = roomTextStyle
            )
            val speakerColored = subtitleColor.copy(alpha = .73f)
            Text(
                text = talk.speakers.joinToString(", ") { it },
                style = subtitleTextStyle,
                color = speakerColored,
            )
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

@ExperimentalMaterial3Api
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
fun TalkItemPreview() {
    Conferences4HallTheme {
        Scaffold {
            TalkItem(talk = TalkItemUi.fake) { }
        }
    }
}

@ExperimentalMaterial3Api
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
fun TalkItemPausePreview() {
    Conferences4HallTheme {
        Scaffold {
            TalkItem(talk = TalkItemUi.fakePause) { }
        }
    }
}
