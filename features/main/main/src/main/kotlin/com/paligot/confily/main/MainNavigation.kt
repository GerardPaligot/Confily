package com.paligot.confily.main

import android.content.res.Configuration
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material3.Icon
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowSize
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.paligot.confily.events.presentation.eventGraph
import com.paligot.confily.infos.presentation.infoGraph
import com.paligot.confily.models.ui.ExportNetworkingUi
import com.paligot.confily.networking.presentation.networkingGraph
import com.paligot.confily.partners.presentation.partnerGraph
import com.paligot.confily.schedules.presentation.scheduleGraph
import com.paligot.confily.speakers.presentation.speakerGraph
import com.paligot.confily.style.components.adaptive.isCompat
import com.paligot.confily.style.theme.appbars.iconColor
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.koinViewModel
import kotlin.reflect.KClass

@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun MainNavigation(
    startDestination: KClass<*>,
    launchUrl: (String) -> Unit,
    onContactExportClicked: (ExportNetworkingUi) -> Unit,
    onReportByPhoneClicked: (String) -> Unit,
    onReportByEmailClicked: (String) -> Unit,
    onShareClicked: (text: String) -> Unit,
    onItineraryClicked: (lat: Double, lng: Double) -> Unit,
    onScheduleStarted: () -> Unit,
    onProfileCreated: () -> Unit,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    viewModel: MainNavigationViewModel = koinViewModel()
) {
    val config = LocalConfiguration.current
    val uiState = viewModel.uiState.collectAsState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val windowSize = with(LocalDensity.current) { currentWindowSize().toSize().toDpSize() }
    val adaptiveInfo = WindowSizeClass.calculateFromSize(windowSize)
    val heightCompact = adaptiveInfo.heightSizeClass.isCompat
    val layoutType = if (heightCompact) {
        NavigationSuiteType.NavigationRail
    } else {
        NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(currentWindowAdaptiveInfo())
    }
    NavigationSuiteScaffold(
        layoutType = layoutType,
        modifier = modifier.semantics {
            testTagsAsResourceId = true
        },
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
                                    popUpTo(navController.graph.findStartDestination().id) {
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
                    isPortrait = config.isPortrait,
                    adaptiveInfo = adaptiveInfo,
                    navController = navController,
                    enterTransition = enterSlideInHorizontal(),
                    popEnterTransition = popEnterSlideInHorizontal(),
                    exitTransition = exitSlideInHorizontal(),
                    popExitTransition = popExistSlideInHorizontal(),
                    onShareClicked = onShareClicked,
                    onItineraryClicked = onItineraryClicked,
                    onScheduleStarted = onScheduleStarted
                )
                speakerGraph(
                    isLandscape = config.orientation == Configuration.ORIENTATION_LANDSCAPE,
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
                    isLandscape = config.orientation == Configuration.ORIENTATION_LANDSCAPE,
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
