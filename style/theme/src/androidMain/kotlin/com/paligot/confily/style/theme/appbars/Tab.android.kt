package com.paligot.confily.style.theme.appbars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.paligot.confily.style.theme.ConfilyTheme

@Preview
@Composable
private fun TabPreview() {
    ConfilyTheme {
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
