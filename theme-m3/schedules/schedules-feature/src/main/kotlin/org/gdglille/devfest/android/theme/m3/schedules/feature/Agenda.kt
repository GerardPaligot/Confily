package org.gdglille.devfest.android.theme.m3.schedules.feature

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.theme.m3.schedules.ui.talks.NoFavoriteTalks
import org.gdglille.devfest.android.theme.m3.schedules.ui.talks.MediumScheduleItem
import org.gdglille.devfest.android.theme.m3.schedules.ui.talks.Time
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.android.theme.m3.style.placeholder
import org.gdglille.devfest.android.theme.m3.style.schedules.findTimeImageVector
import org.gdglille.devfest.android.theme.m3.style.schedules.pause.MediumPauseItem
import org.gdglille.devfest.models.ui.AgendaUi
import org.gdglille.devfest.models.ui.TalkItemUi

@Composable
fun Agenda(
    agenda: AgendaUi,
    onTalkClicked: (id: String) -> Unit,
    onFavoriteClicked: (TalkItemUi) -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
) {
    if (agenda.onlyFavorites && !isLoading && agenda.talks.keys.isEmpty()) {
        NoFavoriteTalks()
    } else {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(vertical = 24.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            agenda.talks.entries.forEach { slot ->
                item {
                    Time(time = slot.key, modifier = Modifier.placeholder(visible = isLoading))
                }
                items(slot.value.toList(), key = { it.id }) {
                    if (it.isPause) {
                        MediumPauseItem(
                            title = it.title,
                            room = it.room,
                            time = it.time,
                            timeImageVector = it.timeInMinutes.findTimeImageVector(),
                            modifier = Modifier.placeholder(visible = isLoading)
                        )
                    } else {
                        MediumScheduleItem(
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
@Preview
@Composable
private fun AgendaPreview() {
    Conferences4HallTheme {
        Scaffold {
            Agenda(
                agenda = AgendaUi.fake,
                onTalkClicked = {},
                onFavoriteClicked = { }
            )
        }
    }
}
