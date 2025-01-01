package com.paligot.confily.infos.presentation

import androidx.compose.animation.fadeIn
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.paligot.confily.events.routes.EventList
import com.paligot.confily.infos.panes.TicketQrCodeScanner
import com.paligot.confily.infos.routes.Info
import com.paligot.confily.infos.routes.ScannerTicket
import com.paligot.confily.infos.routes.TeamMember
import com.paligot.confily.schedules.routes.ScheduleList

fun NavGraphBuilder.infoGraph(
    navController: NavController,
    onReportByPhoneClicked: (String) -> Unit,
    onReportByEmailClicked: (String) -> Unit,
    onItineraryClicked: (lat: Double, lng: Double) -> Unit,
    onQrCodeDetected: (String) -> Unit,
    launchUrl: (String) -> Unit
) {
    composable<Info>(
        enterTransition = { fadeIn() }
    ) {
        InfoCompactVM(
            onItineraryClicked = onItineraryClicked,
            onLinkClicked = { url -> url?.let { launchUrl(it) } },
            onTicketScannerClicked = { navController.navigate(ScannerTicket) },
            onDisconnectedClicked = {
                navController.navigate(EventList) {
                    popUpTo(ScheduleList) {
                        inclusive = true
                    }
                }
            },
            onReportByPhoneClicked = onReportByPhoneClicked,
            onReportByEmailClicked = onReportByEmailClicked,
            onTeamMemberClicked = { navController.navigate(TeamMember(it)) }
        )
    }
    composable<ScannerTicket> {
        TicketQrCodeScanner(
            navigateToSettingsScreen = {},
            onQrCodeDetected = onQrCodeDetected,
            onBackClicked = { navController.popBackStack() }
        )
    }
    composable<TeamMember> {
        TeamMemberVM(onLinkClicked = launchUrl)
    }
}
