package com.paligot.confily.style.schedules.pause

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.style.theme.ConfilyTheme

@Suppress("UnusedPrivateMember")
@Preview
@Composable
private fun SmallPauseItemPreview() {
    ConfilyTheme {
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
    ConfilyTheme {
        MediumPauseItem(
            title = "Break",
            room = "Exposition room",
            time = "60 minutes"
        )
    }
}
