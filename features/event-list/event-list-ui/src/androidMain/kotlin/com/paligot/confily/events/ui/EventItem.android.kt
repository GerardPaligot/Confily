package com.paligot.confily.events.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.models.ui.EventItemUi
import com.paligot.confily.style.theme.ConfilyTheme

@Preview
@Composable
private fun EventItemPreview() {
    ConfilyTheme {
        EventItem(
            item = EventItemUi.fake,
            onClick = {}
        )
    }
}
