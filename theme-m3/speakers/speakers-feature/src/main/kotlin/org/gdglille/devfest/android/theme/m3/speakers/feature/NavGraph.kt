package org.gdglille.devfest.android.theme.m3.speakers.feature

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.paligot.confily.style.components.adaptive.isCompat
import org.gdglille.devfest.theme.m3.navigation.Screen

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
    composable(
        route = Screen.SpeakerList.route,
        enterTransition = { fadeIn() }
    ) {
        SpeakerAdaptive(
            showBackInDetail = adaptiveInfo.widthSizeClass.isCompat,
            onTalkClicked = { navController.navigate(Screen.Schedule.route(it)) },
            onLinkClicked = { launchUrl(it) }
        )
    }
    composable(
        route = Screen.Speaker.route,
        arguments = listOf(navArgument("speakerId") { type = NavType.StringType }),
        enterTransition = { enterTransition },
        popEnterTransition = { popEnterTransition },
        exitTransition = { exitTransition },
        popExitTransition = { popExitTransition }
    ) {
        SpeakerDetailVM(
            speakerId = it.arguments?.getString("speakerId")!!,
            onTalkClicked = { navController.navigate(Screen.Schedule.route(it)) },
            onLinkClicked = { launchUrl(it) },
            navigationIcon = { Back { navController.popBackStack() } },
            isLandscape = isLandscape
        )
    }
}
