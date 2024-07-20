package org.gdglille.devfest.theme.m3.style.schedules.pause

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.TextStyle
import org.gdglille.devfest.android.shared.resources.Resource
import org.gdglille.devfest.android.shared.resources.semantic_pause_item
import org.gdglille.devfest.android.theme.m3.style.tags.MediumTag
import org.gdglille.devfest.android.theme.m3.style.tags.SmallTag
import org.gdglille.devfest.android.theme.m3.style.tags.TagDefaults
import org.gdglille.devfest.android.theme.m3.style.toDp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@Composable
fun SmallPauseItem(
    title: String,
    room: String,
    time: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    timeImageVector: ImageVector = Icons.Outlined.Timer,
    containerColor: Color = PauseItemDefaults.smallContainerColor(onClick != null),
    titleColor: Color = contentColorFor(backgroundColor = containerColor),
    titleTextStyle: TextStyle = PauseItemDefaults.smallTitleTextStyle,
    shape: Shape = PauseItemDefaults.smallShape
) {
    val contentDescription = stringResource(Resource.string.semantic_pause_item, room, time)
    val clickable = if (onClick != null) {
        Modifier.clickable(onClick = onClick)
    } else {
        Modifier
    }
    Surface(shape = shape, color = containerColor, modifier = modifier.then(clickable)) {
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

@OptIn(ExperimentalResourceApi::class)
@Composable
fun MediumPauseItem(
    title: String,
    room: String,
    time: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    timeImageVector: ImageVector = Icons.Outlined.Timer,
    containerColor: Color = PauseItemDefaults.mediumContainerColor(onClick != null),
    titleColor: Color = contentColorFor(backgroundColor = containerColor),
    titleTextStyle: TextStyle = PauseItemDefaults.mediumTitleTextStyle,
    shape: Shape = PauseItemDefaults.mediumShape
) {
    val contentDescription = stringResource(Resource.string.semantic_pause_item, room, time)
    val clickable = if (onClick != null) {
        Modifier.clickable(onClick = onClick)
    } else {
        Modifier
    }
    Surface(shape = shape, color = containerColor, modifier = modifier.then(clickable)) {
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
