package org.gdglille.devfest.android.theme.m3.style.appbars

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.launch
import org.gdglille.devfest.android.theme.m3.style.actions.TabActionsUi
import org.gdglille.devfest.android.theme.m3.style.actions.TopAction
import org.gdglille.devfest.android.theme.m3.style.actions.TopActionsUi

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarContentLayout(
    title: String,
    modifier: Modifier = Modifier,
    topActions: TopActionsUi = TopActionsUi(),
    tabActions: TabActionsUi = TabActionsUi(),
    onActionClicked: (TopAction) -> Unit = {},
    pagerState: PagerState = rememberPagerState(pageCount = { tabActions.actions.count() }),
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Column(modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)) {
        TopAppBar(
            title = title,
            topActionsUi = topActions,
            onActionClicked = onActionClicked,
            scrollBehavior = scrollBehavior,
            windowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Horizontal)
        )
        if (tabActions.actions.count() > 1) {
            val tabSelectedIndex = pagerState.currentPage
            ScrollableTabRow(
                selectedTabIndex = tabSelectedIndex,
                divider = {},
                indicator = {}
            ) {
                tabActions.actions.forEachIndexed { index, tabAction ->
                    Tab(
                        selected = index == tabSelectedIndex,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(tabActions.actions.indexOf(tabAction))
                            }
                        },
                        text = tabAction.label ?: kotlin.run { stringResource(tabAction.labelId) }
                    )
                }
            }
        }
        content()
    }
}
