package com.paligot.confily.web.main

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.paligot.confily.events.presentation.EventListVM
import com.paligot.confily.navigation.Screen
import com.paligot.confily.schedules.presentation.ScheduleDetailEventSessionVM
import com.paligot.confily.schedules.presentation.ScheduleDetailOrientableVM
import com.paligot.confily.schedules.presentation.ScheduleGridVM
import com.paligot.confily.speakers.presentation.SpeakerDetailVM
import com.paligot.confily.speakers.presentation.SpeakersGridVM
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
fun NavGraphBuilder.mainNavGraph(
    onScheduleStarted: () -> Unit,
    onLinkClicked: (url: String) -> Unit,
    onShareClicked: (text: String) -> Unit,
    onItineraryClicked: (lat: Double, lng: Double) -> Unit,
    navController: NavController
) {
    composable(Screen.EventList.route) {
        EventListVM(
            onEventClicked = { navController.navigate(Screen.ScheduleList.route) }
        )
    }
    composable(Screen.ScheduleList.route) {
        ScheduleGridVM(
            onScheduleStarted = onScheduleStarted,
            onTalkClicked = { navController.navigate(Screen.Schedule.route(it)) },
            onEventSessionClicked = { navController.navigate(Screen.ScheduleEvent.route(it)) },
            onFilterClicked = {},
            showFilterIcon = false
        )
    }
    composable(
        route = Screen.Schedule.route,
        arguments = listOf(navArgument("scheduleId") { type = NavType.StringType })
    ) {
        ScheduleDetailOrientableVM(
            scheduleId = it.arguments?.getString("scheduleId")!!,
            onBackClicked = { navController.popBackStack() },
            onSpeakerClicked = { navController.navigate(Screen.Speaker.route(it)) },
            onShareClicked = onShareClicked,
            isLandscape = true
        )
    }
    composable(
        route = Screen.ScheduleEvent.route,
        arguments = listOf(navArgument("scheduleId") { type = NavType.StringType })
    ) {
        ScheduleDetailEventSessionVM(
            scheduleId = it.arguments?.getString("scheduleId")!!,
            onItineraryClicked = onItineraryClicked,
            onBackClicked = { navController.popBackStack() }
        )
    }
    composable(Screen.SpeakerList.route) {
        SpeakersGridVM(
            onSpeakerClicked = { navController.navigate(Screen.Speaker.route(it)) }
        )
    }
    composable(Screen.Speaker.route) { backStackEntry ->
        SpeakerDetailVM(
            speakerId = backStackEntry.arguments?.getString("speakerId")!!,
            onLinkClicked = onLinkClicked,
            onTalkClicked = { navController.navigate(Screen.Schedule.route(it)) }
        )
    }
}
