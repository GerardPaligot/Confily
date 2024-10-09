package com.paligot.confily.wear.presentation.partners.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.material3.HorizontalPageIndicator
import kotlinx.collections.immutable.persistentListOf

@Composable
fun HorizontalPartnersPager(
    modelUi: PartnersModelUi,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit
) {
    val pagerState = rememberPagerState { modelUi.partners.size }
    Box {
        HorizontalPager(
            modifier = modifier.fillMaxSize(),
            state = pagerState
        ) { page ->
            val key = modelUi.partners.keys.toList()[page]
            val partners = modelUi.partners[key] ?: persistentListOf()
            PartnersPane(title = key, partners = partners, onClick = onClick)
        }
        HorizontalPageIndicator(
            pageCount = modelUi.partners.size,
            currentPage = pagerState.currentPage,
            currentPageOffsetFraction = { pagerState.currentPageOffsetFraction }
        )
    }
}