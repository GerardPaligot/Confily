package com.paligot.confily.partners.panes

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.paligot.confily.partners.ui.models.PartnersActivitiesUi
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.screen_partners
import com.paligot.confily.style.theme.Scaffold
import com.paligot.confily.style.theme.actions.TabActionsUi
import org.jetbrains.compose.resources.stringResource

@Composable
fun PartnersActivitiesPager(
    uiModel: PartnersActivitiesUi,
    onPartnerClick: (id: String) -> Unit,
    modifier: Modifier = Modifier,
    tabActionsUi: TabActionsUi = TabActionsUi(),
    pagerState: PagerState = rememberPagerState(pageCount = { tabActionsUi.actions.count() }),
    partnersState: LazyGridState = rememberLazyGridState(),
    isLoading: Boolean = false
) {
    Scaffold(
        title = stringResource(Resource.string.screen_partners),
        modifier = modifier,
        tabActions = tabActionsUi,
        pagerState = pagerState,
        hasScrollBehavior = false
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .padding(top = it.calculateTopPadding())
                .fillMaxSize(),
            verticalAlignment = Alignment.Top
        ) { page ->
            when (page) {
                0 -> PartnersGridContent(
                    partners = uiModel.partners,
                    isLoading = isLoading,
                    onPartnerClick = onPartnerClick,
                    state = partnersState
                )
                1 -> PartnersActivitiesContent(activities = uiModel.activities)
            }
        }
    }
}
