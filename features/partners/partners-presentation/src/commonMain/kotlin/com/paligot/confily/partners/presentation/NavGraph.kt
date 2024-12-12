package com.paligot.confily.partners.presentation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.paligot.confily.partners.routes.Partner
import com.paligot.confily.partners.routes.PartnerList
import com.paligot.confily.style.components.adaptive.isCompat

@Suppress("LongParameterList")
fun NavGraphBuilder.partnerGraph(
    isLandscape: Boolean,
    adaptiveInfo: WindowSizeClass,
    navController: NavController,
    enterTransition: EnterTransition,
    popEnterTransition: EnterTransition,
    exitTransition: ExitTransition,
    popExitTransition: ExitTransition,
    onItineraryClicked: (lat: Double, lng: Double) -> Unit,
    launchUrl: (String) -> Unit
) {
    composable<PartnerList>(
        enterTransition = { fadeIn() }
    ) {
        PartnersAdaptive(
            showBackInDetail = adaptiveInfo.widthSizeClass.isCompat,
            onItineraryClicked = onItineraryClicked,
            onLinkClicked = launchUrl
        )
    }
    composable<Partner>(
        deepLinks = listOf(
            navDeepLink { uriPattern = Partner.navDeeplink() }
        ),
        enterTransition = { enterTransition },
        popEnterTransition = { popEnterTransition },
        exitTransition = { exitTransition },
        popExitTransition = { popExitTransition }
    ) {
        PartnerDetailVM(
            partnerId = it.toRoute<Partner>().id,
            onLinkClicked = launchUrl,
            onItineraryClicked = onItineraryClicked,
            navigationIcon = { Back { navController.popBackStack() } },
            isLandscape = isLandscape
        )
    }
}
