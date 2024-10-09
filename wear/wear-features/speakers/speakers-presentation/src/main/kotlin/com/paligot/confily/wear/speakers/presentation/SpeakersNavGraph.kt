package com.paligot.confily.wear.speakers.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.wear.compose.navigation.composable

fun NavGraphBuilder.speakersGraph(
    navController: NavController
) {
    composable("speakers") {
        SpeakersVM(
            onClick = { navController.navigate("speakers/$it") }
        )
    }
    composable("speakers/{speakerId}") { backStackEntry ->
        SpeakerVM(
            speakerId = backStackEntry.arguments?.getString("speakerId")!!,
            onSessionClick = { navController.navigate("schedules/$it") }
        )
    }
}
