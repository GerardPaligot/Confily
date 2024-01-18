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
import org.gdglille.devfest.android.theme.m3.schedules.ui.talks.MediumScheduleItem
import org.gdglille.devfest.android.theme.m3.schedules.ui.talks.NoFavoriteTalks
import org.gdglille.devfest.android.theme.m3.schedules.ui.talks.SmallScheduleItem
import org.gdglille.devfest.android.theme.m3.schedules.ui.talks.Time
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.android.theme.m3.style.SpacingTokens
import org.gdglille.devfest.android.theme.m3.style.placeholder
import org.gdglille.devfest.android.theme.m3.style.previews.PHONE_LANDSCAPE
import org.gdglille.devfest.android.theme.m3.style.schedules.findTimeImageVector
import org.gdglille.devfest.android.theme.m3.style.schedules.pause.MediumPauseItem
import org.gdglille.devfest.android.theme.m3.style.schedules.pause.SmallPauseItem
import org.gdglille.devfest.android.theme.m3.style.toDp
import org.gdglille.devfest.models.ui.AgendaUi
import org.gdglille.devfest.models.ui.TalkItemUi

@Composable
fun ScheduleGridScreen(
    agenda: AgendaUi,
    onTalkClicked: (id: String) -> Unit,
    onFavoriteClicked: (TalkItemUi) -> Unit,
    modifier: Modifier = Modifier,
    columnCount: Int = 2,
    isSmallSize: Boolean = false,
    isLoading: Boolean = false,
) {
    if (agenda.onlyFavorites && !isLoading && agenda.talks.keys.isEmpty()) {
        NoFavoriteTalks()
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(count = columnCount),
            modifier = modifier,
            contentPadding = PaddingValues(vertical = SpacingTokens.LargeSpacing.toDp()),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.MediumSpacing.toDp()),
            horizontalArrangement = Arrangement.spacedBy(SpacingTokens.MediumSpacing.toDp())
        ) {
            agenda.talks.entries.forEach { slot ->
                item(span = { GridItemSpan(columnCount) }) {
                    Time(time = slot.key, modifier = Modifier.placeholder(visible = isLoading))
                }
                items(slot.value.toList(), key = { it.id }) {
                    if (it.isPause) {
                        if (isSmallSize) {
                            SmallPauseItem(
                                title = it.title,
                                room = it.room,
                                time = it.time,
                                timeImageVector = it.timeInMinutes.findTimeImageVector(),
                                modifier = Modifier.placeholder(visible = isLoading)
                            )
                        } else {
                            MediumPauseItem(
                                title = it.title,
                                room = it.room,
                                time = it.time,
                                timeImageVector = it.timeInMinutes.findTimeImageVector(),
                                modifier = Modifier.placeholder(visible = isLoading)
                            )
                        }
                    } else {
                        if (isSmallSize) {
                            SmallScheduleItem(
                                talk = it,
                                modifier = Modifier.placeholder(visible = isLoading),
                                onFavoriteClicked = onFavoriteClicked,
                                onTalkClicked = onTalkClicked
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
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(device = PHONE_LANDSCAPE)
@Composable
private fun ScheduleListScreenPreview() {
    Conferences4HallTheme {
        Scaffold {
            ScheduleGridScreen(
                agenda = AgendaUi.fake,
                onTalkClicked = {},
                onFavoriteClicked = { }
            )
        }
    }
}
