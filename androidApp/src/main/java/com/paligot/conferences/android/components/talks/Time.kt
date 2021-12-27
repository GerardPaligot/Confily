package com.paligot.conferences.android.components.talks

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.conferences.android.theme.ConferenceTheme

@Composable
fun Time(
    time: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.body1,
    color: Color = MaterialTheme.colors.secondary
) {
    Text(
        text = time,
        modifier = modifier,
        style = style,
        color = color
    )
}

@Preview
@Composable
fun TimePreview() {
    ConferenceTheme {
        Time(time = "10:00")
    }
}
