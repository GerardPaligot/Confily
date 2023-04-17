package org.gdglille.devfest.android.components.talks

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.Grade
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.material3.Card
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
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.components.speakers.HorizontalSpeakersList
import org.gdglille.devfest.android.components.tags.DecorativeTag
import org.gdglille.devfest.android.components.tags.LevelTag
import org.gdglille.devfest.android.components.tags.Tag
import org.gdglille.devfest.android.components.tags.TagDefaults
import org.gdglille.devfest.android.theme.Conferences4HallTheme
import org.gdglille.devfest.android.ui.resources.R
import org.gdglille.devfest.models.TalkItemUi

const val ShortTalk = 30

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TalkItem(
    talk: TalkItemUi,
    modifier: Modifier = Modifier,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    titleTextStyle: TextStyle = MaterialTheme.typography.titleMedium,
    favoriteColor: Color = if (talk.isFavorite) MaterialTheme.colorScheme.secondary
    else MaterialTheme.colorScheme.onBackground,
    onFavoriteClicked: (TalkItemUi) -> Unit = {},
    onTalkClicked: (String) -> Unit
) {
    val semanticSpeakers = if (talk.speakers.isEmpty()) ""
    else stringResource(id = R.string.semantic_talk_item_speakers, talk.speakers.joinToString(", "))
    val semanticLevel = if (talk.level == null) ""
    else stringResource(id = R.string.semantic_talk_item_level, talk.level!!)
    val semanticTalk = stringResource(
        id = R.string.semantic_talk_item,
        talk.title,
        semanticSpeakers,
        talk.room,
        talk.timeInMinutes,
        talk.category.name,
        semanticLevel
    )
    Card(
        shape = MaterialTheme.shapes.medium,
        onClick = { onTalkClicked(talk.id) },
        modifier = modifier
    ) {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clearAndSetSemantics { contentDescription = semanticTalk }
            ) {
                if (talk.category.color != null || talk.level != null) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        talk.category.color?.let {
                            DecorativeTag(category = talk.category)
                        }
                        talk.level?.let {
                            LevelTag(level = it)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Text(
                    text = talk.title,
                    color = titleColor,
                    style = titleTextStyle
                )
                if (talk.speakers.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalSpeakersList(
                        names = talk.speakers,
                        avatars = talk.speakersAvatar,
                        modifier = Modifier
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Tag(
                        text = talk.room,
                        icon = Icons.Outlined.Videocam,
                        colors = TagDefaults.unStyledColors()
                    )
                    Tag(
                        text = stringResource(R.string.text_schedule_minutes, talk.timeInMinutes.toString()),
                        icon = if (talk.timeInMinutes <= ShortTalk) {
                            Icons.Outlined.Bolt
                        } else {
                            Icons.Outlined.Timer
                        },
                        colors = TagDefaults.unStyledColors()
                    )
                }
            }
            if (!talk.isPause) {
                IconButton(
                    onClick = { onFavoriteClicked(talk) },
                    modifier = Modifier
                        .padding(4.dp)
                        .align(Alignment.TopEnd)
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
