package com.paligot.confily.style.schedules.time

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Composable
fun Time(
    time: String,
    modifier: Modifier = Modifier,
    style: TextStyle = TimeDefaults.style,
    color: Color = TimeDefaults.color
) {
    Text(
        text = time,
        modifier = modifier,
        style = style,
        color = color
    )
}
