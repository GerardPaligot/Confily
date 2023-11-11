package org.gdglille.devfest.android.theme.m3.schedules.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.theme.m3.schedules.ui.talks.NoFavoriteTalks
import org.gdglille.devfest.android.theme.m3.schedules.ui.talks.SmallScheduleItem
import org.gdglille.devfest.android.theme.m3.schedules.ui.talks.Time
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.android.theme.m3.style.placeholder
import org.gdglille.devfest.android.theme.m3.style.previews.PHONE_LANDSCAPE
import org.gdglille.devfest.android.theme.m3.style.schedules.findTimeImageVector
import org.gdglille.devfest.android.theme.m3.style.schedules.pause.SmallPauseItem
import org.gdglille.devfest.models.ui.AgendaUi
import org.gdglille.devfest.models.ui.TalkItemUi

@Composable
fun ScheduleListHorizontalScreen(
    agenda: AgendaUi,
    onTalkClicked: (id: String) -> Unit,
    onFavoriteClicked: (TalkItemUi) -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
) {
    if (agenda.onlyFavorites && !isLoading && agenda.talks.keys.isEmpty()) {
        NoFavoriteTalks()
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier,
            contentPadding = PaddingValues(vertical = 24.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            agenda.talks.entries.forEach { slot ->
                item(span = { GridItemSpan(2) }) {
                    Time(time = slot.key, modifier = Modifier.placeholder(visible = isLoading))
                }
                items(slot.value.toList(), key = { it.id }) {
                    if (it.isPause) {
                        SmallPauseItem(
                            title = it.title,
                            room = it.room,
                            time = it.time,
                            timeImageVector = it.timeInMinutes.findTimeImageVector(),
                            modifier = Modifier.placeholder(visible = isLoading)
                        )
                    } else {
                        SmallScheduleItem(
                            talk = it,
                            modifier = Modifier.placeholder(visible = isLoading),
                            onFavoriteClicked = onFavoriteClicked,
                            onTalkClicked = onTalkClicked
                        )
                    }
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(device = PHONE_LANDSCAPE)
@Composable
private fun ScheduleListHorizontalScreenPreview() {
    Conferences4HallTheme {
        Scaffold {
            ScheduleListHorizontalScreen(
                agenda = AgendaUi.fake,
                onTalkClicked = {},
                onFavoriteClicked = { }
            )
        }
    }
}

