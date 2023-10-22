package org.gdglille.devfest.android.theme.m3.style.schedules.card

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.android.theme.m3.style.R
import org.gdglille.devfest.android.theme.m3.style.speakers.avatars.SmallLabeledSpeakersAvatar
import org.gdglille.devfest.android.theme.m3.style.toDp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleCard(
    title: String,
    speakersUrls: ImmutableList<String>,
    speakersLabel: String,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isFavorite: Boolean = false,
    colors: ScheduleCardColors = ScheduleCardDefaults.cardColors(),
    style: TextStyle = ScheduleCardDefaults.style,
    shape: Shape = ScheduleCardDefaults.shape,
    onFavoriteClick: (() -> Unit)? = null,
    topBar: (@Composable RowScope.() -> Unit)? = null,
    bottomBar: (@Composable RowScope.() -> Unit)? = null
) {
    val semanticModifier = if (contentDescription != null)
        Modifier.clearAndSetSemantics { this.contentDescription = contentDescription }
    else Modifier
    Card(shape = shape, onClick = onClick, modifier = modifier) {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(ScheduleCardTokens.ContainerPadding.toDp())
                    .then(semanticModifier)
            ) {
                if (topBar != null) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        content = topBar
                    )
                    Spacer(modifier = Modifier.height(ScheduleCardTokens.TopBarBottomPadding.toDp()))
                }
                Text(text = title, color = colors.titleColor, style = style)
                if (speakersUrls.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(ScheduleCardTokens.SpeakersTopPadding.toDp()))
                    SmallLabeledSpeakersAvatar(label = speakersLabel, urls = speakersUrls)
                }
                Spacer(modifier = Modifier.height(ScheduleCardTokens.SpeakersBottomPadding.toDp()))
                if (bottomBar != null) {
                    Row(content = bottomBar)
                }
            }
            if (onFavoriteClick != null) {
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier
                        .padding(ScheduleCardTokens.IconButtonFavoritePadding.toDp())
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Outlined.Star else Icons.Outlined.Grade,
                        contentDescription = if (isFavorite) stringResource(R.string.action_favorites_remove)
                        else stringResource(R.string.action_favorites_add),
                        tint = colors.favIconColor(isFavorite = isFavorite).value
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
private fun ScheduleCardPreview() {
    Conferences4HallTheme {
        ScheduleCard(
            title = "Designers x Developers : Ça match \uD83D\uDC99 ou ça match \uD83E\uDD4A ?",
            speakersUrls = persistentListOf("", ""),
            speakersLabel = "John Doe and Jeanne Doe",
            contentDescription = null,
            onClick = {},
            onFavoriteClick = {}
        )
    }
}

@ExperimentalMaterial3Api
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
private fun ScheduleCardFavoritePreview() {
    Conferences4HallTheme {
        ScheduleCard(
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
