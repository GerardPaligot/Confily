package org.gdglille.devfest.theme.m3.style.schedules.pause

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.style.theme.Conferences4HallTheme

@Suppress("UnusedPrivateMember")
@Preview
@Composable
private fun SmallPauseItemPreview() {
    Conferences4HallTheme {
        SmallPauseItem(
            title = "Break",
            room = "Exposition room",
            time = "60 minutes"
        )
    }
}

@Suppress("UnusedPrivateMember")
@Preview
@Composable
private fun MediumPauseItemPreview() {
    Conferences4HallTheme {
        MediumPauseItem(
            title = "Break",
            room = "Exposition room",
            time = "60 minutes"
        )
    }
}
