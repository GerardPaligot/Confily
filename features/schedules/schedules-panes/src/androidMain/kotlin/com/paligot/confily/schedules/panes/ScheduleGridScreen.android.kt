package com.paligot.confily.schedules.panes

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.schedules.panes.models.AgendaUi
import com.paligot.confily.style.theme.ConfilyTheme
import com.paligot.confily.style.theme.previews.PHONE_LANDSCAPE

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
private fun ScheduleListScreenPreview() {
    ConfilyTheme {
        Scaffold {
            ScheduleGridScreen(
                agenda = AgendaUi.fake,
                isRefreshing = true,
                onTalkClicked = {},
                onEventSessionClicked = {},
                onFavoriteClicked = { },
                onRefresh = {}
            )
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(device = PHONE_LANDSCAPE)
@Composable
private fun ScheduleListScreenLandscapePreview() {
    ConfilyTheme {
        Scaffold {
            ScheduleGridScreen(
                agenda = AgendaUi.fake,
                isRefreshing = false,
                onTalkClicked = {},
                onEventSessionClicked = {},
                onFavoriteClicked = { },
                onRefresh = {}
            )
        }
    }
}
