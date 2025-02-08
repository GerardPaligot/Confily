package com.paligot.confily.schedules.presentation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.paligot.confily.schedules.routes.Schedule
import com.paligot.confily.schedules.routes.ScheduleEvent
import com.paligot.confily.schedules.routes.ScheduleFilters
import com.paligot.confily.schedules.routes.ScheduleList
import com.paligot.confily.speakers.routes.Speaker
import com.paligot.confily.style.components.adaptive.isCompat
import com.paligot.confily.style.components.adaptive.isMedium
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Suppress("LongParameterList")
fun NavGraphBuilder.scheduleGraph(
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
    composable<ScheduleList>(
        enterTransition = { fadeIn() },
        exitTransition = { exitTransition }
    ) {
        val scope = rememberCoroutineScope(getContext = { Dispatchers.Main })
        val showFilterIcon = adaptiveInfo.widthSizeClass.isCompat ||
            (adaptiveInfo.widthSizeClass.isMedium && isPortrait)
        val isSmallSize = adaptiveInfo.heightSizeClass.isCompat
        ScheduleGridAdaptive(
            onScheduleStarted = onScheduleStarted,
            onFilterClicked = { scope.launch { navController.navigate(ScheduleFilters) } },
            onTalkClicked = { scope.launch { navController.navigate(Schedule(it)) } },
            onEventSessionClicked = { scope.launch { navController.navigate(ScheduleEvent(it)) } },
            showFilterIcon = showFilterIcon,
            isSmallSize = isSmallSize
        )
    }
    composable<ScheduleFilters> {
        val scope = rememberCoroutineScope(getContext = { Dispatchers.Main })
        AgendaFiltersCompactVM(
            navigationIcon = { Back { scope.launch { navController.popBackStack() } } }
        )
    }
    composable<Schedule>(
        deepLinks = listOf(
            navDeepLink { uriPattern = Schedule.navDeeplink() }
        ),
        enterTransition = { enterTransition },
        popEnterTransition = { popEnterTransition },
        exitTransition = { exitTransition },
        popExitTransition = { popExitTransition }
    ) {
        val scope = rememberCoroutineScope(getContext = { Dispatchers.Main })
        ScheduleDetailOrientableVM(
            scheduleId = it.toRoute<Schedule>().id,
            onBackClicked = { scope.launch { navController.popBackStack() } },
            onSpeakerClicked = { scope.launch { navController.navigate(Speaker(id = it)) } },
            onShareClicked = onShareClicked,
            isLandscape = isPortrait.not()
        )
    }
    composable<ScheduleEvent>(
        enterTransition = { enterTransition },
        popEnterTransition = { popEnterTransition },
        exitTransition = { exitTransition },
        popExitTransition = { popExitTransition }
    ) {
        val scope = rememberCoroutineScope(getContext = { Dispatchers.Main })
        ScheduleDetailEventSessionVM(
            scheduleId = it.toRoute<ScheduleEvent>().id,
            onItineraryClicked = onItineraryClicked,
            onBackClicked = { scope.launch { navController.popBackStack() } }
        )
    }
}
