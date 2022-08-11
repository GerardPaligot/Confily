package org.gdglille.devfest.android.theme.vitamin.features.structure

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.decathlon.vitamin.compose.appbars.topbars.icons.VitaminNavigationIconButtons
import org.gdglille.devfest.android.theme.vitamin.ui.components.structure.VitaminScaffold
import org.gdglille.devfest.android.ui.resources.BottomAction
import org.gdglille.devfest.android.ui.resources.FabAction
import org.gdglille.devfest.android.ui.resources.TabAction
import org.gdglille.devfest.android.ui.resources.TopAction

@Composable
fun VitaminScaffoldNavigation(
    @StringRes title: Int,
    startDestination: String,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    topActions: List<TopAction> = emptyList(),
    tabActions: List<TabAction> = emptyList(),
    bottomActions: List<BottomAction> = emptyList(),
    fabAction: FabAction? = null,
    scrollable: Boolean = false,
    onTopActionClicked: (TopAction) -> Unit = {},
    onTabClicked: (TabAction) -> Unit = {},
    onBottomActionClicked: (BottomAction) -> Unit = {},
    onFabActionClicked: (FabAction) -> Unit = {},
    navigationIcon: @Composable (VitaminNavigationIconButtons.() -> Unit)? = null,
    builder: NavGraphBuilder.() -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val route = currentDestination?.route ?: startDestination
    VitaminScaffold(
        title = title,
        modifier = modifier,
        topActions = topActions,
        tabActions = tabActions,
        bottomActions = bottomActions,
        fabAction = fabAction,
        scrollable = scrollable,
        routeSelected = route,
        onTopActionClicked = onTopActionClicked,
        onTabClicked = {
            navController.navigate(it.route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                restoreState = true
            }
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
        navigationIcon = navigationIcon,
        content = {
            NavHost(
                navController,
                startDestination = startDestination,
                modifier = modifier.padding(it),
                builder = builder
            )
        }
    )
}
