package com.paligot.confily.speakers.sample

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.paligot.confily.speakers.presentation.speakerGraph
import com.paligot.confily.speakers.routes.SpeakerList
import com.paligot.confily.style.components.adaptive.adaptiveInfo
import com.paligot.confily.style.theme.ConfilyTheme

@Composable
fun App(
    isLandscape: Boolean,
    launchUrl: (String) -> Unit
) {
    val navController = rememberNavController()
    val adaptiveInfo = adaptiveInfo()
    ConfilyTheme {
        NavHost(
            navController = navController,
            startDestination = SpeakerList::class,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            builder = {
                speakerGraph(
                    isLandscape = isLandscape,
                    adaptiveInfo = adaptiveInfo,
                    navController = navController,
                    enterTransition = EnterTransition.None,
                    popEnterTransition = EnterTransition.None,
                    exitTransition = ExitTransition.None,
                    popExitTransition = ExitTransition.None,
                    launchUrl = launchUrl
                )
            }
        )
    }
}
