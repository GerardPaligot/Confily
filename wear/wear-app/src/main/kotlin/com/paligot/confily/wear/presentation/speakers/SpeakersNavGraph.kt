package com.paligot.confily.wear.presentation.speakers

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.wear.compose.navigation.composable
import com.paligot.confily.wear.presentation.speakers.presentation.SpeakerVM
import com.paligot.confily.wear.presentation.speakers.presentation.SpeakersVM

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
