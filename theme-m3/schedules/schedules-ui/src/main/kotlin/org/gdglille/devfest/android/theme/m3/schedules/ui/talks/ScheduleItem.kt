package org.gdglille.devfest.android.theme.m3.schedules.ui.talks

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.gdglille.devfest.android.shared.resources.Resource
import org.gdglille.devfest.android.shared.resources.semantic_talk_item
import org.gdglille.devfest.android.shared.resources.semantic_talk_item_level
import org.gdglille.devfest.android.shared.resources.semantic_talk_item_speakers
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.android.theme.m3.style.schedules.card.MediumScheduleCard
import org.gdglille.devfest.android.theme.m3.style.schedules.card.SmallScheduleCard
import org.gdglille.devfest.android.theme.m3.style.schedules.findCategoryImageVector
import org.gdglille.devfest.android.theme.m3.style.schedules.findTimeImageVector
import org.gdglille.devfest.android.theme.m3.style.tags.MediumAutoColoredTag
import org.gdglille.devfest.android.theme.m3.style.tags.MediumTag
import org.gdglille.devfest.android.theme.m3.style.tags.SmallAutoColoredTag
import org.gdglille.devfest.android.theme.m3.style.tags.SmallTag
import org.gdglille.devfest.android.theme.m3.style.tags.TagDefaults
import org.gdglille.devfest.models.ui.TalkItemUi
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalLayoutApi::class, ExperimentalResourceApi::class)
@Composable
fun SmallScheduleItem(
    talk: TalkItemUi,
    modifier: Modifier = Modifier,
    onFavoriteClicked: (TalkItemUi) -> Unit = {},
    onTalkClicked: (String) -> Unit
) {
    val semanticSpeakers = if (talk.speakers.isEmpty()) {
        ""
    } else {
        stringResource(Resource.string.semantic_talk_item_speakers, talk.speakers.joinToString(", "))
    }
    val semanticLevel = if (talk.level == null) {
        ""
    } else {
        stringResource(Resource.string.semantic_talk_item_level, talk.level!!)
    }
    val semanticTalk = stringResource(
        Resource.string.semantic_talk_item,
        talk.title,
        semanticSpeakers,
        talk.room,
        talk.timeInMinutes,
        talk.category.name,
        semanticLevel
    )
    SmallScheduleCard(
        title = talk.title,
        speakersUrls = talk.speakersAvatar,
        speakersLabel = talk.speakersLabel,
        contentDescription = semanticTalk,
        isFavorite = talk.isFavorite,
        onClick = { onTalkClicked(talk.id) },
        onFavoriteClick = { onFavoriteClicked(talk) },
        bottomBar = {
            SmallAutoColoredTag(
                text = talk.category.name,
                colorName = talk.category.color,
                icon = talk.category.icon?.findCategoryImageVector()
            )
            talk.level?.let {
                SmallTag(text = it, colors = TagDefaults.gravelColors())
            }
            SmallTag(
                text = talk.room,
                icon = Icons.Outlined.Videocam,
                colors = TagDefaults.unStyledColors()
            )
            SmallTag(
                text = talk.time,
                icon = talk.timeInMinutes.findTimeImageVector(),
                colors = TagDefaults.unStyledColors()
            )
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun MediumScheduleItem(
    talk: TalkItemUi,
    modifier: Modifier = Modifier,
    onFavoriteClicked: (TalkItemUi) -> Unit = {},
    onTalkClicked: (String) -> Unit
) {
    val semanticSpeakers = if (talk.speakers.isEmpty()) {
        ""
    } else {
        stringResource(Resource.string.semantic_talk_item_speakers, talk.speakers.joinToString(", "))
    }
    val semanticLevel = if (talk.level == null) {
        ""
    } else {
        stringResource(Resource.string.semantic_talk_item_level, talk.level!!)
    }
    val semanticTalk = stringResource(
        Resource.string.semantic_talk_item,
        talk.title,
        semanticSpeakers,
        talk.room,
        talk.timeInMinutes,
        talk.category.name,
        semanticLevel
    )
    MediumScheduleCard(
        title = talk.title,
        speakersUrls = talk.speakersAvatar,
        speakersLabel = talk.speakersLabel,
        contentDescription = semanticTalk,
        isFavorite = talk.isFavorite,
        onClick = { onTalkClicked(talk.id) },
        onFavoriteClick = { onFavoriteClicked(talk) },
        topBar = {
            MediumAutoColoredTag(
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

@Suppress("UnusedPrivateMember")
@Preview
@Composable
private fun SmallTalkItemPreview() {
    Conferences4HallTheme {
        SmallScheduleItem(talk = TalkItemUi.fake) { }
    }
}

@Suppress("UnusedPrivateMember")
@Preview
@Composable
private fun MediumTalkItemPreview() {
    Conferences4HallTheme {
        MediumScheduleItem(talk = TalkItemUi.fake) { }
    }
}
