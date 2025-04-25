package com.paligot.confily.wear.events.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.foundation.pager.HorizontalPager
import androidx.wear.compose.foundation.pager.rememberPagerState
import androidx.wear.compose.material3.HorizontalPageIndicator
import androidx.wear.compose.material3.HorizontalPagerScaffold

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
        HorizontalPagerScaffold(
            pagerState = pagerState,
            pageIndicator = { HorizontalPageIndicator(pagerState = pagerState) }
        ) {
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
        }
    }
}
