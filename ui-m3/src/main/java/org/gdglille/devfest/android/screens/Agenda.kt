package org.gdglille.devfest.android.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.components.talks.NoFavoriteTalks
import org.gdglille.devfest.android.components.talks.ScheduleItem
import org.gdglille.devfest.android.theme.Conferences4HallTheme
import org.gdglille.devfest.models.AgendaUi
import org.gdglille.devfest.models.TalkItemUi

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Agenda(
    agenda: AgendaUi,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    onTalkClicked: (id: String) -> Unit,
    onFavoriteClicked: (TalkItemUi) -> Unit
) {
    if (agenda.onlyFavorites && !isLoading && agenda.talks.keys.isEmpty()) {
        NoFavoriteTalks()
    } else {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
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
        }
    }
}

@ExperimentalMaterial3Api
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
fun AgendaPreview() {
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
