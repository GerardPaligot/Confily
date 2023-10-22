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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.android.theme.m3.style.SpacingTokens
import org.gdglille.devfest.android.theme.m3.style.tags.Tag
import org.gdglille.devfest.android.theme.m3.style.tags.TagDefaults
import org.gdglille.devfest.android.theme.m3.style.toDp

@Composable
fun PauseItem(
    title: String,
    room: String,
    time: String,
    modifier: Modifier = Modifier,
    timeImageVector: ImageVector = Icons.Outlined.Timer,
    containerColor: Color = PauseItemDefaults.containerColor,
    titleColor: Color = contentColorFor(backgroundColor = containerColor),
    titleTextStyle: TextStyle = PauseItemDefaults.titleTextStyle,
    shape: Shape = PauseItemDefaults.shape
) {
    Surface(shape = shape, color = containerColor, modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.MediumSpacing.toDp()),
            modifier = Modifier
                .fillMaxWidth()
                .padding(SpacingTokens.LargeSpacing.toDp())
            // TODO .clearAndSetSemantics { contentDescription = semanticTalk }
        ) {
            Text(
                text = title,
                color = titleColor,
                style = titleTextStyle
            )
            Row {
                Tag(
                    text = room,
                    icon = Icons.Outlined.Place,
                    colors = TagDefaults.unStyledColors()
                )
                Tag(
                    text = time,
                    icon = timeImageVector,
                    colors = TagDefaults.unStyledColors()
                )
            }
        }
    }
}

@Preview
@Composable
private fun PauseItemPreview() {
    Conferences4HallTheme {
        PauseItem(title = "Break", room = "Exposition room", time = "60 minutes")
    }
}
