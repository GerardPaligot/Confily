package com.paligot.confily.wear.partners.panes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.foundation.pager.HorizontalPager
import androidx.wear.compose.foundation.pager.rememberPagerState
import androidx.wear.compose.material3.HorizontalPageIndicator
import androidx.wear.compose.material3.HorizontalPagerScaffold
import kotlinx.collections.immutable.persistentListOf

@Composable
fun HorizontalPartnersPager(
    modelUi: PartnersModelUi,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit
) {
    val pagerState = rememberPagerState { modelUi.partners.size }
    Box {
        HorizontalPagerScaffold(
            pagerState = pagerState,
            pageIndicator = { HorizontalPageIndicator(pagerState = pagerState) },
            content = {
                HorizontalPager(
                    modifier = modifier.fillMaxSize(),
                    state = pagerState
                ) { page ->
                    val key = modelUi.partners.keys.toList()[page]
                    val partners = modelUi.partners[key] ?: persistentListOf()
                    PartnersPane(title = key, partners = partners, onClick = onClick)
                }
            }
        )
    }
}
