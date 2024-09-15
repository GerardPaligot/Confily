package com.paligot.confily.web.main

import androidx.compose.material3.Icon
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.paligot.confily.navigation.BottomActions
import com.paligot.confily.navigation.Screen
import com.paligot.confily.style.theme.appbars.iconColor
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun Main(
    startDestination: String,
    onLinkClicked: (url: String) -> Unit,
    onShareClicked: (text: String) -> Unit,
    onItineraryClicked: (lat: Double, lng: Double) -> Unit,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    mainViewModel: MainViewModel = koinViewModel()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val route = currentDestination?.route ?: Screen.ScheduleList.route
    val navActions = remember(route) {
        if (route == Screen.EventList.route) persistentListOf()
        else persistentListOf(BottomActions.agenda, BottomActions.speakers)
    }
    NavigationSuiteScaffold(
        modifier = modifier,
        layoutType = NavigationSuiteType.NavigationRail,
        navigationSuiteItems = {
            navActions.forEach { action ->
                val selected = action.route == route
                item(
                    selected = selected,
                    icon = {
                        Icon(
                            imageVector = if (selected) action.iconSelected else action.icon,
                            contentDescription = stringResource(action.label),
                            tint = iconColor(selected = selected)
                        )
                    },
                    onClick = {
                        navController.navigate(action.route) {
                            popUpTo(navController.graph.findStartDestination().route!!) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        },
        content = {
            NavHost(
                navController = navController,
                startDestination = startDestination,
                builder = {
                    mainNavGraph(
                        onScheduleStarted = mainViewModel::loadAgenda,
                        onLinkClicked = onLinkClicked,
                        onShareClicked = onShareClicked,
                        onItineraryClicked = onItineraryClicked,
                        navController = navController
                    )
                }
            )
        }
    )
}
