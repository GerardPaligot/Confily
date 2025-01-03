package com.paligot.confily.networking.presentation

import androidx.compose.animation.fadeIn
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.paligot.confily.networking.routes.MyProfile
import com.paligot.confily.networking.routes.NewProfile
import com.paligot.confily.networking.routes.ScannerVCard
import com.paligot.confily.networking.ui.models.ExportNetworkingUi
import com.paligot.confily.networking.ui.models.VCardModel

fun NavGraphBuilder.networkingGraph(
    navController: NavController,
    onContactExportClicked: (ExportNetworkingUi) -> Unit,
    onProfileCreated: () -> Unit,
    onQrCodeDetected: (VCardModel) -> Unit
) {
    composable<MyProfile>(
        deepLinks = listOf(
            navDeepLink { uriPattern = MyProfile.navDeeplink() }
        ),
        enterTransition = { fadeIn() }
    ) {
        NetworkingCompactVM(
            onCreateProfileClicked = { navController.navigate(NewProfile) },
            onContactScannerClicked = { navController.navigate(ScannerVCard) },
            onContactExportClicked = onContactExportClicked
        )
    }
    composable<NewProfile>(
        deepLinks = listOf(
            navDeepLink { uriPattern = NewProfile.navDeeplink() }
        )
    ) {
        ProfileInputVM(
            onBackClicked = { navController.popBackStack() },
            onProfileCreated = {
                onProfileCreated()
                navController.popBackStack()
            }
        )
    }
    composable<ScannerVCard> {
        VCardQrCodeScanner(
            navigateToSettingsScreen = {},
            onQrCodeDetected = onQrCodeDetected,
            onBackClicked = { navController.popBackStack() }
        )
    }
}
