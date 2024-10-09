package com.paligot.confily.wear.schedules.panes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.material3.HorizontalPageIndicator

@Composable
fun HorizontalSchedulePager(
    modelUi: ScheduleModelUi,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit
) {
    val pageCount = modelUi.sessions.size
    val pagerState = rememberPagerState { pageCount }
    Box {
        HorizontalPager(
            modifier = modifier.fillMaxSize(),
            state = pagerState
        ) { page ->
            val key = modelUi.sessions.keys.toList()[page]
            val sessions = modelUi.sessions[key]
            SchedulesPane(title = key, sessionsUi = sessions, onClick = onClick)
        }
        HorizontalPageIndicator(
            pageCount = pageCount,
            currentPage = pagerState.currentPage,
            currentPageOffsetFraction = { pagerState.currentPageOffsetFraction }
        )
    }
}
