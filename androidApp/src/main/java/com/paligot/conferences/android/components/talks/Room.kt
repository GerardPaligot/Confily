package com.paligot.conferences.android.components.talks

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import com.paligot.conferences.android.theme.ConferenceTheme

@Composable
fun Room(
    room: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.onBackground,
    style: TextStyle = MaterialTheme.typography.overline,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        FaIcon(faIcon = FaIcons.Video, tint = color, size = 12.dp)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = room, color = color, style = style)
    }
}

@Preview
@Composable
fun RoomPreview() {
    ConferenceTheme {
        Room("Stage 2")
    }
}
