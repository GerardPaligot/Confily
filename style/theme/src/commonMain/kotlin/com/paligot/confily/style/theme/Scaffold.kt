package com.paligot.confily.style.theme

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.paligot.confily.style.theme.actions.FabAction
import com.paligot.confily.style.theme.actions.TabActionsUi
import com.paligot.confily.style.theme.actions.TopAction
import com.paligot.confily.style.theme.actions.TopActionsUi
import com.paligot.confily.style.theme.appbars.AppBarIcons
import com.paligot.confily.style.theme.appbars.Tab
import com.paligot.confily.style.theme.appbars.TopAppBar
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Scaffold(
    title: String,
    modifier: Modifier = Modifier,
    hasScrollBehavior: Boolean = true,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    topActions: TopActionsUi = TopActionsUi(),
    tabActions: TabActionsUi = TabActionsUi(),
    fabAction: FabAction? = null,
    onActionClicked: (TopAction) -> Unit = {},
    onFabActionClicked: (FabAction) -> Unit = {},
    pagerState: PagerState = rememberPagerState(pageCount = { tabActions.actions.count() }),
    navigationIcon: @Composable (AppBarIcons.() -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val scrollModifier = if (hasScrollBehavior) {
        Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    } else {
        Modifier
    }
    Scaffold(
        modifier = modifier.then(scrollModifier),
        containerColor = containerColor,
        contentColor = contentColor,
        topBar = {
            Column {
                TopAppBar(
                    title = title,
                    topActionsUi = topActions,
                    onActionClicked = onActionClicked,
                    scrollBehavior = scrollBehavior,
                    navigationIcon = navigationIcon,
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = containerColor)
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
                                        pagerState.animateScrollToPage(
                                            tabActions.actions.indexOf(tabAction)
                                        )
                                    }
                                },
                                text = tabAction.label
                                    ?: kotlin.run { stringResource(tabAction.labelId) }
                            )
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            fabAction?.let {
                ExtendedFloatingActionButton(
                    text = { Text(text = stringResource(fabAction.label)) },
                    icon = {
                        Icon(
                            imageVector = fabAction.icon,
                            contentDescription = fabAction.contentDescription
                                ?.let { stringResource(it) }
                        )
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    onClick = { onFabActionClicked(fabAction) }
                )
            }
        },
        content = content
    )
}
