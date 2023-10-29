package org.gdglille.devfest.android.theme.m3.schedules.ui.talks

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.android.theme.m3.style.R
import org.gdglille.devfest.android.theme.m3.style.schedules.card.ScheduleCard
import org.gdglille.devfest.android.theme.m3.style.schedules.findCategoryImageVector
import org.gdglille.devfest.android.theme.m3.style.schedules.findTimeImageVector
import org.gdglille.devfest.android.theme.m3.style.tags.AutoColoredTag
import org.gdglille.devfest.android.theme.m3.style.tags.MediumTag
import org.gdglille.devfest.android.theme.m3.style.tags.TagDefaults
import org.gdglille.devfest.models.ui.TalkItemUi

@Composable
fun ScheduleItem(
    talk: TalkItemUi,
    modifier: Modifier = Modifier,
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
    ScheduleCard(
        title = talk.title,
        speakersUrls = talk.speakersAvatar,
        speakersLabel = talk.speakersLabel,
        contentDescription = semanticTalk,
        isFavorite = talk.isFavorite,
        onClick = { onTalkClicked(talk.id) },
        onFavoriteClick = { onFavoriteClicked(talk) },
        topBar = {
            AutoColoredTag(
                text = talk.category.name,
                colorName = talk.category.color,
                icon = talk.category.icon?.findCategoryImageVector()
            )
            talk.level?.let {
                MediumTag(text = it, colors = TagDefaults.gravelColors())
            }
        },
        bottomBar = {
            MediumTag(
                text = talk.room,
                icon = Icons.Outlined.Videocam,
                colors = TagDefaults.unStyledColors()
            )
            MediumTag(
                text = talk.time,
                icon = talk.timeInMinutes.findTimeImageVector(),
                colors = TagDefaults.unStyledColors()
            )
        },
        modifier = modifier
    )
}

@Preview
@Composable
private fun TalkItemPreview() {
    Conferences4HallTheme {
        ScheduleItem(talk = TalkItemUi.fake) { }
    }
}
