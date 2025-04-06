package com.paligot.confily.schedules.panes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.paligot.confily.schedules.panes.models.AgendaUi
import com.paligot.confily.schedules.semantics.SchedulesSemantics
import com.paligot.confily.schedules.ui.models.EventSessionItemUi
import com.paligot.confily.schedules.ui.models.TalkItemUi
import com.paligot.confily.schedules.ui.talks.MediumScheduleItem
import com.paligot.confily.schedules.ui.talks.NoFavoriteTalks
import com.paligot.confily.schedules.ui.talks.SmallScheduleItem
import com.paligot.confily.style.components.placeholder.placeholder
import com.paligot.confily.style.schedules.findTimeImageVector
import com.paligot.confily.style.schedules.pause.MediumPauseItem
import com.paligot.confily.style.schedules.pause.SmallPauseItem
import com.paligot.confily.style.schedules.time.Time
import com.paligot.confily.style.theme.SpacingTokens
import com.paligot.confily.style.theme.toDp
import kotlin.math.floor

const val NbHorizontalPadding = 2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleGridScreen(
    agenda: AgendaUi,
    isRefreshing: Boolean,
    onTalkClicked: (id: String) -> Unit,
    onEventSessionClicked: (id: String) -> Unit,
    onFavoriteClicked: (TalkItemUi) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    state: LazyGridState = rememberLazyGridState(),
    isSmallSize: Boolean = false,
    isLoading: Boolean = false
) {
    if (agenda.onlyFavorites && !isLoading && agenda.sessions.keys.isEmpty()) {
        NoFavoriteTalks(modifier = modifier)
    } else {
        BoxWithConstraints(modifier = modifier) {
            val minSize = 260.dp
            val mediumSpacing = SpacingTokens.MediumSpacing.toDp()
            val widthSize = this.maxWidth - (mediumSpacing * NbHorizontalPadding)
            val count = floor(widthSize / minSize).toInt()
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = onRefresh
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = minSize),
                    modifier = Modifier.testTag(SchedulesSemantics.list),
                    state = state,
                    contentPadding = PaddingValues(
                        vertical = SpacingTokens.LargeSpacing.toDp(),
                        horizontal = SpacingTokens.MediumSpacing.toDp()
                    ),
                    verticalArrangement = Arrangement.spacedBy(mediumSpacing),
                    horizontalArrangement = Arrangement.spacedBy(mediumSpacing)
                ) {
                    agenda.sessions.entries.forEach { slot ->
                        item(span = { GridItemSpan(count) }) {
                            Time(
                                time = slot.key,
                                modifier = Modifier.placeholder(visible = isLoading)
                            )
                        }
                        items(slot.value.toList(), key = { it.id }) {
                            when (it) {
                                is EventSessionItemUi -> {
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
                                            modifier = Modifier.placeholder(visible = isLoading),
                                            onClick = if (it.isClickable) {
                                                {
                                                    onEventSessionClicked(it.id)
                                                }
                                            } else {
                                                null
                                            }
                                        )
                                    }
                                }

                                is TalkItemUi -> {
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
        }
    }
}
