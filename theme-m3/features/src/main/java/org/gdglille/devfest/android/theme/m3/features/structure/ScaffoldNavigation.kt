package org.gdglille.devfest.android.theme.m3.features.structure

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import org.gdglille.devfest.android.components.structure.Scaffold
import org.gdglille.devfest.android.ui.resources.actions.BottomAction
import org.gdglille.devfest.android.ui.resources.actions.FabAction
import org.gdglille.devfest.android.ui.resources.actions.TabAction
import org.gdglille.devfest.android.ui.resources.actions.TopAction
import org.gdglille.devfest.android.ui.resources.models.BottomActionsUi
import org.gdglille.devfest.android.ui.resources.models.TabActionsUi
import org.gdglille.devfest.android.ui.resources.models.TopActionsUi

@ExperimentalPagerApi
@Composable
fun ScaffoldNavigation(
    @StringRes title: Int,
    startDestination: String,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    pagerState: PagerState = rememberPagerState(),
    topActions: TopActionsUi = TopActionsUi(),
    tabActions: TabActionsUi = TabActionsUi(),
    bottomActions: BottomActionsUi = BottomActionsUi(),
    fabAction: FabAction? = null,
    onTopActionClicked: (TopAction) -> Unit = {},
    onTabClicked: (TabAction) -> Unit = {},
    onBottomActionClicked: (BottomAction) -> Unit = {},
    onFabActionClicked: (FabAction) -> Unit = {},
    builder: NavGraphBuilder.() -> Unit
) {
    val scope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val route = currentDestination?.route ?: startDestination
    Scaffold(
        title = title,
        modifier = modifier,
        topActions = topActions,
        tabActions = tabActions,
        bottomActions = bottomActions,
        fabAction = fabAction,
        routeSelected = route,
        tabSelectedIndex = pagerState.currentPage,
        onTopActionClicked = onTopActionClicked,
        onTabClicked = {
            scope.launch { pagerState.animateScrollToPage(tabActions.actions.indexOf(it)) }
            onTabClicked(it)
        },
        onBottomActionClicked = {
            navController.navigate(it.route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
            onBottomActionClicked(it)
        },
        onFabActionClicked = onFabActionClicked,
        content = {
            NavHost(
                navController,
                startDestination = startDestination,
                modifier = Modifier.padding(it),
                builder = builder
            )
        }
    )
}
