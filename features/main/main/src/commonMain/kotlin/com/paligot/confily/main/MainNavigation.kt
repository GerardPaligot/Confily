package com.paligot.confily.main

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material3.Icon
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.paligot.confily.events.presentation.eventGraph
import com.paligot.confily.infos.presentation.infoGraph
import com.paligot.confily.networking.presentation.networkingGraph
import com.paligot.confily.networking.ui.models.ExportNetworkingUi
import com.paligot.confily.partners.presentation.partnerGraph
import com.paligot.confily.schedules.presentation.scheduleGraph
import com.paligot.confily.schedules.routes.ScheduleList
import com.paligot.confily.speakers.presentation.speakerGraph
import com.paligot.confily.style.components.adaptive.isCompat
import com.paligot.confily.style.theme.appbars.iconColor
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.reflect.KClass

@Suppress("LongMethod")
@Composable
fun MainNavigation(
    startDestination: KClass<*>,
    isPortrait: Boolean,
    adaptiveInfo: WindowSizeClass,
    launchUrl: (String) -> Unit,
    onContactExportClicked: (ExportNetworkingUi) -> Unit,
    onReportByPhoneClicked: (String) -> Unit,
    onReportByEmailClicked: (String) -> Unit,
    onShareClicked: (text: String) -> Unit,
    onItineraryClicked: (lat: Double, lng: Double) -> Unit,
    onProfileCreated: () -> Unit,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    viewModel: MainNavigationViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val heightCompact = adaptiveInfo.heightSizeClass.isCompat
    val layoutType = if (heightCompact) {
        NavigationSuiteType.NavigationRail
    } else {
        NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(currentWindowAdaptiveInfo())
    }
    NavigationSuiteScaffold(
        layoutType = layoutType,
        modifier = modifier,
        navigationSuiteItems = {
            when (uiState.value) {
                is MainNavigationUiState.Success -> {
                    val navActions = (uiState.value as MainNavigationUiState.Success).navActions
                    navActions.actions.forEach { action ->
                        val selected = currentDestination?.hasRoute(action.route::class) ?: false
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
                                    popUpTo(ScheduleList) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }

                else -> {}
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            builder = {
                eventGraph(navController)
                scheduleGraph(
                    isPortrait = isPortrait,
                    adaptiveInfo = adaptiveInfo,
                    navController = navController,
                    enterTransition = enterSlideInHorizontal(),
                    popEnterTransition = popEnterSlideInHorizontal(),
                    exitTransition = exitSlideInHorizontal(),
                    popExitTransition = popExistSlideInHorizontal(),
                    onShareClicked = onShareClicked,
                    onItineraryClicked = onItineraryClicked
                )
                speakerGraph(
                    isLandscape = isPortrait.not(),
                    adaptiveInfo = adaptiveInfo,
                    navController = navController,
                    enterTransition = enterSlideInHorizontal(),
                    popEnterTransition = popEnterSlideInHorizontal(),
                    exitTransition = exitSlideInHorizontal(),
                    popExitTransition = popExistSlideInHorizontal(),
                    launchUrl = launchUrl
                )
                networkingGraph(
                    navController = navController,
                    onContactExportClicked = onContactExportClicked,
                    onProfileCreated = onProfileCreated,
                    onQrCodeDetected = {
                        viewModel.saveNetworkingProfile(it)
                        navController.popBackStack()
                    }
                )
                partnerGraph(
                    isLandscape = isPortrait.not(),
                    adaptiveInfo = adaptiveInfo,
                    navController = navController,
                    enterTransition = enterSlideInHorizontal(),
                    popEnterTransition = popEnterSlideInHorizontal(),
                    exitTransition = exitSlideInHorizontal(),
                    popExitTransition = popExistSlideInHorizontal(),
                    onItineraryClicked = onItineraryClicked,
                    launchUrl = launchUrl
                )
                infoGraph(
                    navController = navController,
                    onReportByPhoneClicked = onReportByPhoneClicked,
                    onReportByEmailClicked = onReportByEmailClicked,
                    onItineraryClicked = onItineraryClicked,
                    onQrCodeDetected = { barcode ->
                        viewModel.saveTicket(barcode)
                        navController.popBackStack()
                    },
                    launchUrl = launchUrl
                )
            }
        )
    }
}
