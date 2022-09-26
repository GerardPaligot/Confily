package org.gdglille.devfest.android.theme.m3.features.structure

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
import org.gdglille.devfest.android.components.structure.Scaffold
import org.gdglille.devfest.android.ui.resources.actions.BottomAction
import org.gdglille.devfest.android.ui.resources.actions.TopAction
import org.gdglille.devfest.android.ui.resources.models.BottomActionsUi
import org.gdglille.devfest.android.ui.resources.models.TopActionsUi

@Composable
fun ScaffoldNavigation(
    @StringRes title: Int,
    startDestination: String,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    topActions: TopActionsUi = TopActionsUi(),
    bottomActions: BottomActionsUi = BottomActionsUi(),
    onTopActionClicked: (TopAction) -> Unit = {},
    onBottomActionClicked: (BottomAction) -> Unit = {},
    builder: NavGraphBuilder.() -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val route = currentDestination?.route ?: startDestination
    Scaffold(
        title = title,
        modifier = modifier,
        topActions = topActions,
        bottomActions = bottomActions,
        routeSelected = route,
        onTopActionClicked = onTopActionClicked,
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
