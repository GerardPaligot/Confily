package com.paligot.confily.wear.presentation.schedules.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.ScrollIndicator
import androidx.wear.compose.material3.Text
import com.paligot.confily.wear.presentation.schedules.ui.EventSessionItem
import com.paligot.confily.wear.presentation.schedules.ui.ScheduleCardItem
import com.paligot.confily.wear.theme.tags.Tag
import com.paligot.confily.wear.theme.tags.findCategoryImageVector
import kotlinx.collections.immutable.ImmutableList

@Composable
fun SchedulesPane(
    title: String,
    sessionsUi: ImmutableList<SessionModelUi>?,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit
) {
    val scrollState = rememberScalingLazyListState()
    ScreenScaffold(
        scrollState = scrollState,
        modifier = modifier,
        scrollIndicator = {
            ScrollIndicator(scrollState, modifier = Modifier.align(Alignment.CenterEnd))
        }
    ) {
        ScalingLazyColumn(
            contentPadding = PaddingValues(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            item { ListHeader { Text(title) } }
            if (sessionsUi != null) {
                items(sessionsUi) {
                    SessionItem(sessionModelUi = it, onClick = onClick)
                }
            }
        }
    }
}

@Composable
private fun SessionItem(
    sessionModelUi: SessionModelUi,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit
) {
    when (sessionModelUi) {
        is EventSessionModelUi -> {
            EventSessionItem(
                title = sessionModelUi.title,
                timeSlot = sessionModelUi.timeSlot,
                timeDuration = sessionModelUi.timeDuration,
                modifier = modifier
            )
        }

        is ScheduleSessionModelUi -> {
            ScheduleCardItem(
                title = sessionModelUi.title,
                timeSlot = sessionModelUi.timeSlot,
                timeDuration = sessionModelUi.timeDuration,
                bottomBar = {
                    Tag(
                        text = sessionModelUi.categoryUi.name,
                        icon = sessionModelUi.categoryUi.icon?.findCategoryImageVector()
                    )
                },
                onClick = { onClick(sessionModelUi.id) },
                modifier = modifier
            )
        }
    }
}
