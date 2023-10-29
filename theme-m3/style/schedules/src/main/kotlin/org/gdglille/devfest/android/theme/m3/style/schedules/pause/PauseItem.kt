package org.gdglille.devfest.android.theme.m3.style.schedules.pause

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.TextStyle
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.android.theme.m3.style.R
import org.gdglille.devfest.android.theme.m3.style.previews.ThemedPreviews
import org.gdglille.devfest.android.theme.m3.style.tags.MediumTag
import org.gdglille.devfest.android.theme.m3.style.tags.SmallTag
import org.gdglille.devfest.android.theme.m3.style.tags.TagDefaults
import org.gdglille.devfest.android.theme.m3.style.toDp

@Composable
fun SmallPauseItem(
    title: String,
    room: String,
    time: String,
    modifier: Modifier = Modifier,
    timeImageVector: ImageVector = Icons.Outlined.Timer,
    containerColor: Color = PauseItemDefaults.smallContainerColor,
    titleColor: Color = contentColorFor(backgroundColor = containerColor),
    titleTextStyle: TextStyle = PauseItemDefaults.smallTitleTextStyle,
    shape: Shape = PauseItemDefaults.smallShape
) {
    val contentDescription = stringResource(id = R.string.semantic_pause_item, room, time)
    Surface(shape = shape, color = containerColor, modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.spacedBy(PauseItemSmallTokens.BetweenSpacing.toDp()),
            modifier = Modifier
                .fillMaxWidth()
                .padding(PauseItemDefaults.smallContentPadding)
                .clearAndSetSemantics { this.contentDescription = contentDescription }
        ) {
            Text(
                text = title,
                color = titleColor,
                style = titleTextStyle
            )
            Row {
                SmallTag(
                    text = room,
                    icon = Icons.Outlined.Place,
                    colors = TagDefaults.unStyledColors()
                )
                SmallTag(
                    text = time,
                    icon = timeImageVector,
                    colors = TagDefaults.unStyledColors()
                )
            }
        }
    }
}

@Composable
fun MediumPauseItem(
    title: String,
    room: String,
    time: String,
    modifier: Modifier = Modifier,
    timeImageVector: ImageVector = Icons.Outlined.Timer,
    containerColor: Color = PauseItemDefaults.mediumContainerColor,
    titleColor: Color = contentColorFor(backgroundColor = containerColor),
    titleTextStyle: TextStyle = PauseItemDefaults.mediumTitleTextStyle,
    shape: Shape = PauseItemDefaults.mediumShape
) {
    val contentDescription = stringResource(id = R.string.semantic_pause_item, room, time)
    Surface(shape = shape, color = containerColor, modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.spacedBy(PauseItemMediumTokens.BetweenSpacing.toDp()),
            modifier = Modifier
                .fillMaxWidth()
                .padding(PauseItemMediumTokens.ContainerPadding.toDp())
                .clearAndSetSemantics { this.contentDescription = contentDescription }
        ) {
            Text(
                text = title,
                color = titleColor,
                style = titleTextStyle
            )
            Row {
                MediumTag(
                    text = room,
                    icon = Icons.Outlined.Place,
                    colors = TagDefaults.unStyledColors()
                )
                MediumTag(
                    text = time,
                    icon = timeImageVector,
                    colors = TagDefaults.unStyledColors()
                )
            }
        }
    }
}

@ThemedPreviews
@Composable
private fun SmallPauseItemPreview() {
    Conferences4HallTheme {
        SmallPauseItem(
            title = "Break",
            room = "Exposition room",
            time = "60 minutes"
        )
    }
}

@ThemedPreviews
@Composable
private fun MediumPauseItemPreview() {
    Conferences4HallTheme {
        MediumPauseItem(
            title = "Break",
            room = "Exposition room",
            time = "60 minutes"
        )
    }
}
