package org.gdglille.devfest.android.theme.m3.schedules.feature

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import kotlinx.collections.immutable.ImmutableList
import org.gdglille.devfest.models.ui.AgendaUi
import org.gdglille.devfest.models.ui.TalkItemUi

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScheduleListOrientable(
    agendas: ImmutableList<AgendaUi>,
    pagerState: PagerState,
    onTalkClicked: (id: String) -> Unit,
    onFavoriteClicked: (TalkItemUi) -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
) {
    val orientation = LocalConfiguration.current
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.Top
    ) { page ->
        if (orientation.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ScheduleListHorizontal(
                agenda = agendas[page],
                modifier = modifier,
                onTalkClicked = onTalkClicked,
                onFavoriteClicked = onFavoriteClicked,
                isLoading = isLoading
            )
        } else {
            ScheduleListVertical(
                agenda = agendas[page],
                modifier = modifier,
                onTalkClicked = onTalkClicked,
                onFavoriteClicked = onFavoriteClicked,
                isLoading = isLoading
            )
        }
    }
}
