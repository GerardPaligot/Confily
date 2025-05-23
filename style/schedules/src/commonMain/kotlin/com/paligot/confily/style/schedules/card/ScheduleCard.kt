package com.paligot.confily.style.schedules.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Grade
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.action_favorites_add
import com.paligot.confily.resources.action_favorites_remove
import com.paligot.confily.style.speakers.avatars.SmallLabeledSpeakersAvatar
import com.paligot.confily.style.theme.ConfilyTheme
import com.paligot.confily.style.theme.toDp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SmallScheduleCard(
    title: String,
    speakersUrls: ImmutableList<String>,
    speakersLabel: String,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isFavorite: Boolean = false,
    colors: ScheduleCardColors = ScheduleCardDefaults.cardColors(),
    style: TextStyle = ScheduleCardDefaults.smallTextStyle,
    shape: Shape = ScheduleCardDefaults.smallShape,
    onFavoriteClick: (() -> Unit)? = null,
    bottomBar: (@Composable FlowRowScope.() -> Unit)? = null
) {
    val semanticModifier = if (contentDescription != null) {
        Modifier.clearAndSetSemantics { this.contentDescription = contentDescription }
    } else {
        Modifier
    }
    Card(shape = shape, onClick = onClick, modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.spacedBy(ScheduleCardSmallTokens.SpeakersBottomPadding.toDp()),
            modifier = Modifier.padding(ScheduleCardSmallTokens.ContainerPadding.toDp())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .then(semanticModifier)
                ) {
                    Text(text = title, color = colors.titleColor, style = style)
                    if (speakersUrls.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(ScheduleCardSmallTokens.SpeakersTopPadding.toDp()))
                        SmallLabeledSpeakersAvatar(label = speakersLabel, urls = speakersUrls)
                    }
                }
                if (onFavoriteClick != null) {
                    IconButton(
                        onClick = onFavoriteClick,
                        modifier = Modifier.padding(ScheduleCardSmallTokens.IconButtonFavoritePadding.toDp())
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Outlined.Star else Icons.Outlined.Grade,
                            contentDescription =
                            if (isFavorite) {
                                stringResource(Resource.string.action_favorites_remove)
                            } else {
                                stringResource(Resource.string.action_favorites_add)
                            },
                            tint = colors.favIconColor(isFavorite = isFavorite).value
                        )
                    }
                }
            }
            if (bottomBar != null) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(
                        ScheduleCardSmallTokens.BetweenBottomBarItemPadding.toDp()
                    ),
                    verticalArrangement = Arrangement.spacedBy(
                        ScheduleCardSmallTokens.BetweenBottomBarItemPadding.toDp()
                    ),
                    content = bottomBar
                )
            }
        }
    }
}

@Composable
fun MediumScheduleCard(
    title: String,
    speakersUrls: ImmutableList<String>,
    speakersLabel: String,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isFavorite: Boolean = false,
    colors: ScheduleCardColors = ScheduleCardDefaults.cardColors(),
    style: TextStyle = ScheduleCardDefaults.mediumTextStyle,
    shape: Shape = ScheduleCardDefaults.mediumShape,
    onFavoriteClick: (() -> Unit)? = null,
    topBar: (@Composable RowScope.() -> Unit)? = null,
    bottomBar: (@Composable RowScope.() -> Unit)? = null
) {
    val semanticModifier = if (contentDescription != null) {
        Modifier.clearAndSetSemantics { this.contentDescription = contentDescription }
    } else {
        Modifier
    }
    Card(shape = shape, onClick = onClick, modifier = modifier) {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(ScheduleCardMediumTokens.ContainerPadding.toDp())
                    .then(semanticModifier)
            ) {
                if (topBar != null) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        content = topBar
                    )
                    Spacer(modifier = Modifier.height(ScheduleCardMediumTokens.TopBarBottomPadding.toDp()))
                }
                Text(text = title, color = colors.titleColor, style = style)
                if (speakersUrls.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(ScheduleCardMediumTokens.SpeakersTopPadding.toDp()))
                    SmallLabeledSpeakersAvatar(label = speakersLabel, urls = speakersUrls)
                }
                Spacer(modifier = Modifier.height(ScheduleCardMediumTokens.SpeakersBottomPadding.toDp()))
                if (bottomBar != null) {
                    Row(content = bottomBar)
                }
            }
            if (onFavoriteClick != null) {
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier
                        .padding(ScheduleCardMediumTokens.IconButtonFavoritePadding.toDp())
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Outlined.Star else Icons.Outlined.Grade,
                        contentDescription = if (isFavorite) {
                            stringResource(Resource.string.action_favorites_remove)
                        } else {
                            stringResource(Resource.string.action_favorites_add)
                        },
                        tint = colors.favIconColor(isFavorite = isFavorite).value
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
private fun SmallScheduleCardPreview() {
    ConfilyTheme {
        SmallScheduleCard(
            title = "Designers x Developers : Ça match \uD83D\uDC99 ou ça match \uD83E\uDD4A ?",
            speakersUrls = persistentListOf("", ""),
            speakersLabel = "John Doe and Jeanne Doe",
            contentDescription = null,
            onClick = {},
            onFavoriteClick = {}
        )
    }
}

@Preview
@Composable
private fun ScheduleCardPreview() {
    ConfilyTheme {
        MediumScheduleCard(
            title = "Designers x Developers : Ça match \uD83D\uDC99 ou ça match \uD83E\uDD4A ?",
            speakersUrls = persistentListOf("", ""),
            speakersLabel = "John Doe and Jeanne Doe",
            contentDescription = null,
            onClick = {},
            onFavoriteClick = {}
        )
    }
}

@Preview
@Composable
private fun ScheduleCardFavoritePreview() {
    ConfilyTheme {
        MediumScheduleCard(
            title = "Designers x Developers : Ça match \uD83D\uDC99 ou ça match \uD83E\uDD4A ?",
            speakersUrls = persistentListOf("", ""),
            speakersLabel = "John Doe and Jeanne Doe",
            isFavorite = true,
            contentDescription = null,
            onClick = {},
            onFavoriteClick = {}
        )
    }
}
