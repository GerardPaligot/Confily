package com.paligot.confily.style.speakers.items

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.style.theme.ConfilyTheme

@Preview
@Composable
private fun SpeakerItemPreview() {
    ConfilyTheme {
        LargeSpeakerItem(
            name = "Gérard Paligot",
            description = "Senior Staff Engineer @Decathlon DIgital",
            url = "",
            onClick = {}
        )
    }
}
