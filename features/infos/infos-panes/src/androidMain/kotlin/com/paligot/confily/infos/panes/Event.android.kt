package com.paligot.confily.infos.panes

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.infos.ui.models.EventUi
import com.paligot.confily.style.theme.ConfilyTheme

@ExperimentalFoundationApi
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
private fun EventPreview() {
    ConfilyTheme {
        Scaffold {
            Event(
                event = EventUi.fake,
                onLinkClicked = {},
                onItineraryClicked = { _, _ -> }
            )
        }
    }
}
