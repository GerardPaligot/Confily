package org.gdglille.devfest.android.theme.m3.style.appbars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme

@Preview
@Composable
fun TabPreview() {
    Conferences4HallTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Tab(
                text = "10 June",
                selected = true,
                onClick = {}
            )
            Tab(
                text = "11 June",
                selected = false,
                onClick = {}
            )
        }
    }
}
