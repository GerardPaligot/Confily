package com.paligot.confily.wear.presentation.schedules.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.LocalContentColor
import androidx.wear.compose.material3.LocalTextStyle
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import com.paligot.confily.wear.theme.ConfilyTheme

@Composable
fun EventSessionItem(
    title: String,
    timeSlot: String,
    timeDuration: String,
    modifier: Modifier = Modifier,
    titleStyle: TextStyle = MaterialTheme.typography.labelMedium,
    metaStyle: TextStyle = MaterialTheme.typography.bodySmall,
    titleColor: Color = MaterialTheme.colorScheme.onBackground,
    metaColor: Color = MaterialTheme.colorScheme.onBackground
) {
    Column(
        modifier = modifier.padding(8.dp),
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
    }
}

@Preview
@Composable
private fun EventSessionItemPreview() {
    ConfilyTheme {
        EventSessionItem(
            title = "Sample Event",
            timeSlot = "08:00",
            timeDuration = "50 min"
        )
    }
}
