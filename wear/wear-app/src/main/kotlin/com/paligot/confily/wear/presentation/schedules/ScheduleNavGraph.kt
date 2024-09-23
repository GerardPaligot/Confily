package com.paligot.confily.wear.presentation.schedules

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.wear.compose.navigation.composable
import com.paligot.confily.wear.presentation.schedules.presentation.ScheduleVM
import com.paligot.confily.wear.presentation.schedules.presentation.SchedulesVM

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
