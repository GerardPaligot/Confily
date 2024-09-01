package org.gdglille.devfest.android.theme.m3.schedules.screens

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.android.theme.m3.style.previews.PHONE_LANDSCAPE
import org.gdglille.devfest.models.ui.AgendaUi

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
private fun ScheduleListScreenPreview() {
    Conferences4HallTheme {
        Scaffold {
            ScheduleGridScreen(
                agenda = AgendaUi.fake,
                onTalkClicked = {},
                onEventSessionClicked = {},
                onFavoriteClicked = { }
            )
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(device = PHONE_LANDSCAPE)
@Composable
private fun ScheduleListScreenLandscapePreview() {
    Conferences4HallTheme {
        Scaffold {
            ScheduleGridScreen(
                agenda = AgendaUi.fake,
                onTalkClicked = {},
                onEventSessionClicked = {},
                onFavoriteClicked = { }
            )
        }
    }
}
