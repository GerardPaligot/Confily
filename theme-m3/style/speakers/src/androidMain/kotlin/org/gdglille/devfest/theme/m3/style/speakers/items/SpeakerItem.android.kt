package org.gdglille.devfest.theme.m3.style.speakers.items

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.style.theme.Conferences4HallTheme

@Preview
@Composable
private fun SpeakerItemPreview() {
    Conferences4HallTheme {
        LargeSpeakerItem(
            name = "Gérard Paligot",
            description = "Senior Staff Engineer @Decathlon DIgital",
            url = "",
            onClick = {}
        )
    }
}
