package com.paligot.confily.wear.presentation.schedules.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.Card
import androidx.wear.compose.material3.LocalContentColor
import androidx.wear.compose.material3.LocalTextStyle
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text

@Composable
fun ScheduleCardItem(
    title: String,
    timeSlot: String,
    timeDuration: String,
    modifier: Modifier = Modifier,
    bottomBar: @Composable RowScope.() -> Unit = {},
    titleStyle: TextStyle = MaterialTheme.typography.labelMedium,
    metaStyle: TextStyle = MaterialTheme.typography.bodySmall,
    titleColor: Color = MaterialTheme.colorScheme.onBackground,
    metaColor: Color = MaterialTheme.colorScheme.onBackground,
    shape: Shape = MaterialTheme.shapes.small,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        shape = shape,
        onClick = onClick
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                CompositionLocalProvider(
                    LocalContentColor provides metaColor,
                    LocalTextStyle provides metaStyle
                ) {
                    Text(text = timeSlot)
                    Text(text = timeDuration)
                }
            }
            Text(
                text = title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = titleStyle,
                color = titleColor
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                content = bottomBar
            )
        }
    }
}
