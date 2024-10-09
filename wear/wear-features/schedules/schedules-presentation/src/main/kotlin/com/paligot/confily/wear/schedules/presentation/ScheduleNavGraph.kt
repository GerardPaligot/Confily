package com.paligot.confily.wear.schedules.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.wear.compose.navigation.composable

fun NavGraphBuilder.scheduleGraph(
    navController: NavController
) {
    composable("schedules") {
        SchedulesVM(
            onClick = { navController.navigate("schedules/$it") }
        )
    }
    composable("schedules/{sessionId}") { backStackEntry ->
        ScheduleVM(
            sessionId = backStackEntry.arguments?.getString("sessionId")!!,
            onSpeakerClick = { navController.navigate("speakers/$it") }
        )
    }
}
