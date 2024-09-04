package org.gdglille.devfest.android.theme.m3.schedules.feature

import android.content.res.Configuration
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.paligot.confily.style.components.adaptive.isCompat
import com.paligot.confily.style.components.adaptive.isMedium
import org.gdglille.devfest.theme.m3.navigation.Screen

@Suppress("LongParameterList")
fun NavGraphBuilder.scheduleGraph(
    rootUri: String,
    isPortrait: Boolean,
    adaptiveInfo: WindowSizeClass,
    navController: NavController,
    enterTransition: EnterTransition,
    popEnterTransition: EnterTransition,
    exitTransition: ExitTransition,
    popExitTransition: ExitTransition,
    onShareClicked: (text: String) -> Unit,
    onItineraryClicked: (lat: Double, lng: Double) -> Unit,
    onScheduleStarted: () -> Unit
) {
    composable(
        route = Screen.ScheduleList.route,
        enterTransition = { fadeIn() },
        exitTransition = { exitTransition }
    ) {
        val showFilterIcon = adaptiveInfo.widthSizeClass.isCompat ||
            (adaptiveInfo.widthSizeClass.isMedium && isPortrait)
        val isSmallSize = adaptiveInfo.heightSizeClass.isCompat
        ScheduleGridAdaptive(
            onScheduleStarted = onScheduleStarted,
            onFilterClicked = { navController.navigate(Screen.ScheduleFilters.route) },
            onTalkClicked = { navController.navigate(Screen.Schedule.route(it)) },
            onEventSessionClicked = { navController.navigate(Screen.ScheduleEvent.route(it)) },
            showFilterIcon = showFilterIcon,
            isSmallSize = isSmallSize
        )
    }
    composable(route = Screen.ScheduleFilters.route) {
        AgendaFiltersCompactVM(
            navigationIcon = { Back { navController.popBackStack() } }
        )
    }
    composable(
        route = Screen.Schedule.route,
        arguments = listOf(navArgument("scheduleId") { type = NavType.StringType }),
        deepLinks = listOf(
            navDeepLink {
                uriPattern = "$rootUri/${Screen.Schedule.route}"
            }
        ),
        enterTransition = { enterTransition },
        popEnterTransition = { popEnterTransition },
        exitTransition = { exitTransition },
        popExitTransition = { popExitTransition }
    ) {
        val orientation = LocalConfiguration.current
        ScheduleDetailOrientableVM(
            scheduleId = it.arguments?.getString("scheduleId")!!,
            onBackClicked = { navController.popBackStack() },
            onSpeakerClicked = { navController.navigate(Screen.Speaker.route(it)) },
            onShareClicked = onShareClicked,
            isLandscape = orientation.orientation == Configuration.ORIENTATION_LANDSCAPE
        )
    }
    composable(
        route = Screen.ScheduleEvent.route,
        arguments = listOf(navArgument("scheduleId") { type = NavType.StringType }),
        enterTransition = { enterTransition },
        popEnterTransition = { popEnterTransition },
        exitTransition = { exitTransition },
        popExitTransition = { popExitTransition }
    ) {
        ScheduleDetailEventSessionVM(
            scheduleId = it.arguments?.getString("scheduleId")!!,
            onItineraryClicked = onItineraryClicked,
            onBackClicked = { navController.popBackStack() }
        )
    }
}
