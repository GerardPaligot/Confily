package com.paligot.confily.speakers.presentation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.paligot.confily.schedules.routes.Schedule
import com.paligot.confily.speakers.routes.Speaker
import com.paligot.confily.speakers.routes.SpeakerList
import com.paligot.confily.style.components.adaptive.isCompat

@Suppress("LongParameterList")
fun NavGraphBuilder.speakerGraph(
    isLandscape: Boolean,
    adaptiveInfo: WindowSizeClass,
    navController: NavController,
    enterTransition: EnterTransition,
    popEnterTransition: EnterTransition,
    exitTransition: ExitTransition,
    popExitTransition: ExitTransition,
    launchUrl: (String) -> Unit
) {
    composable<SpeakerList>(
        enterTransition = { fadeIn() }
    ) {
        SpeakerAdaptive(
            showBackInDetail = adaptiveInfo.widthSizeClass.isCompat,
            onTalkClicked = { navController.navigate(Schedule(it)) },
            onLinkClicked = { launchUrl(it) }
        )
    }
    composable<Speaker>(
        enterTransition = { enterTransition },
        popEnterTransition = { popEnterTransition },
        exitTransition = { exitTransition },
        popExitTransition = { popExitTransition }
    ) {
        SpeakerDetailVM(
            speakerId = it.toRoute<Speaker>().id,
            onTalkClicked = { navController.navigate(Schedule(it)) },
            onLinkClicked = { launchUrl(it) },
            navigationIcon = { Back { navController.popBackStack() } },
            isLandscape = isLandscape
        )
    }
}
