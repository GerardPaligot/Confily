package org.gdglille.devfest.android.theme.vitamin.ui.screens.agenda

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.theme.vitamin.ui.components.talks.NoFavoriteTalks
import org.gdglille.devfest.android.theme.vitamin.ui.components.talks.ScheduleItem
import org.gdglille.devfest.android.theme.vitamin.ui.theme.Conferences4HallTheme
import org.gdglille.devfest.models.AgendaUi
import org.gdglille.devfest.models.TalkItemUi

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Agenda(
    agenda: AgendaUi,
    onTalkClicked: (id: String) -> Unit,
    onFavoriteClicked: (TalkItemUi) -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    if (agenda.onlyFavorites && !isLoading && agenda.talks.keys.isEmpty()) {
        NoFavoriteTalks()
    } else {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(vertical = 24.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(agenda.talks.keys.toList(), key = { it }) {
                ScheduleItem(
                    time = it,
                    talks = agenda.talks[it]!!,
                    isLoading = isLoading,
                    onTalkClicked = onTalkClicked,
                    onFavoriteClicked = onFavoriteClicked,
                    modifier = Modifier.animateItemPlacement()
                )
            }
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Preview
@Composable
fun AgendaPreview() {
    Conferences4HallTheme {
        Scaffold {
            Agenda(
                agenda = AgendaUi.fake,
                onTalkClicked = {},
                onFavoriteClicked = {},
                modifier = Modifier.padding(it)
            )
        }
    }
}
