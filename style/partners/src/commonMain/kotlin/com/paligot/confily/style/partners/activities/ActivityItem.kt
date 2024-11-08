package com.paligot.confily.style.partners.activities

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Handshake
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import com.paligot.confily.style.theme.tags.MediumTag
import com.paligot.confily.style.theme.tags.SmallTag
import com.paligot.confily.style.theme.tags.TagDefaults
import com.paligot.confily.style.theme.toDp

@Composable
fun SmallActivityItem(
    activityName: String,
    partnerName: String,
    time: String,
    modifier: Modifier = Modifier,
    containerColor: Color = ActivityItemDefaults.smallContainerColor,
    titleColor: Color = contentColorFor(backgroundColor = containerColor),
    titleTextStyle: TextStyle = ActivityItemDefaults.smallTitleTextStyle,
    shape: Shape = ActivityItemDefaults.smallShape
) {
    Surface(shape = shape, color = containerColor, modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.spacedBy(ActivityItemSmallTokens.BetweenSpacing.toDp()),
            modifier = Modifier
                .fillMaxWidth()
                .padding(ActivityItemDefaults.smallContentPadding)
                // .clearAndSetSemantics { this.contentDescription = contentDescription }
        ) {
            Text(
                text = activityName,
                color = titleColor,
                style = titleTextStyle
            )
            Row {
                SmallTag(
                    text = time,
                    icon = Icons.Outlined.Schedule,
                    colors = TagDefaults.unStyledColors()
                )
                SmallTag(
                    text = partnerName,
                    icon = Icons.Outlined.Handshake,
                    colors = TagDefaults.unStyledColors()
                )
            }
        }
    }
}

@Composable
fun MediumActivityItem(
    activityName: String,
    partnerName: String,
    time: String,
    modifier: Modifier = Modifier,
    containerColor: Color = ActivityItemDefaults.mediumContainerColor,
    titleColor: Color = contentColorFor(backgroundColor = containerColor),
    titleTextStyle: TextStyle = ActivityItemDefaults.mediumTitleTextStyle,
    shape: Shape = ActivityItemDefaults.mediumShape
) {
    Surface(shape = shape, color = containerColor, modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.spacedBy(ActivityItemMediumTokens.BetweenSpacing.toDp()),
            modifier = Modifier
                .fillMaxWidth()
                .padding(ActivityItemMediumTokens.ContainerPadding.toDp())
                // .clearAndSetSemantics { this.contentDescription = contentDescription }
        ) {
            Text(
                text = activityName,
                color = titleColor,
                style = titleTextStyle
            )
            Row {
                MediumTag(
                    text = time,
                    icon = Icons.Outlined.Schedule,
                    colors = TagDefaults.unStyledColors()
                )
                MediumTag(
                    text = partnerName,
                    icon = Icons.Outlined.Handshake,
                    colors = TagDefaults.unStyledColors()
                )
            }
        }
    }
}
