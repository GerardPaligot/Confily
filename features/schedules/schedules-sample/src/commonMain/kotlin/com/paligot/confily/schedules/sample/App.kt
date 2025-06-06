package com.paligot.confily.schedules.sample

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.paligot.confily.schedules.presentation.scheduleGraph
import com.paligot.confily.schedules.routes.ScheduleList
import com.paligot.confily.style.components.adaptive.adaptiveInfo
import com.paligot.confily.style.theme.ConfilyTheme

@Composable
fun App(
    isPortrait: Boolean,
    adaptiveInfo: WindowSizeClass = adaptiveInfo()
) {
    val navController = rememberNavController()
    ConfilyTheme {
        NavHost(
            navController = navController,
            startDestination = ScheduleList::class,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            builder = {
                scheduleGraph(
                    isPortrait = isPortrait,
                    adaptiveInfo = adaptiveInfo,
                    navController = navController,
                    enterTransition = EnterTransition.None,
                    popEnterTransition = EnterTransition.None,
                    exitTransition = ExitTransition.None,
                    popExitTransition = ExitTransition.None,
                    onShareClicked = {},
                    onItineraryClicked = { _, _ -> }
                )
            }
        )
    }
}
