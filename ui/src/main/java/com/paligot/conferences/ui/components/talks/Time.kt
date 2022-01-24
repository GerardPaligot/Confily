package com.paligot.conferences.ui.components.talks

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.conferences.ui.theme.Conferences4HallTheme

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
    Conferences4HallTheme {
        Time(time = "10:00")
    }
}
