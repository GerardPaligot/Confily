package com.paligot.confily.wear.presentation.events.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.material3.HorizontalPageIndicator

private const val NbPages = 3

@Composable
fun HorizontalEventPager(
    onSchedulesClick: () -> Unit,
    onSpeakersClick: () -> Unit,
    onPartnersClick: () -> Unit,
    onChangeEventClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState { NbPages }
    Box {
        HorizontalPager(
            modifier = modifier.fillMaxSize(),
            state = pagerState
        ) { page ->
            when (page) {
                0 -> EventVM(
                    onSchedulesClick = onSchedulesClick,
                    onSpeakersClick = onSpeakersClick,
                    onPartnersClick = onPartnersClick,
                    onChangeEventClick = onChangeEventClick
                )

                1 -> MenusVM()
                2 -> CoCVM()
            }
        }
        HorizontalPageIndicator(
            pageCount = NbPages,
            currentPage = pagerState.currentPage,
            currentPageOffsetFraction = { pagerState.currentPageOffsetFraction }
        )
    }
}
