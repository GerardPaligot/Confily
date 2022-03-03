package com.paligot.conferences.android.screens.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.paligot.conferences.android.screens.agenda.AgendaVM
import com.paligot.conferences.android.screens.event.EventVM
import com.paligot.conferences.android.screens.users.NetworkingVM
import com.paligot.conferences.repositories.AgendaRepository
import com.paligot.conferences.repositories.UserRepository
import com.paligot.conferences.ui.components.appbars.ActionItemId
import com.paligot.conferences.ui.components.appbars.BottomAppBar
import com.paligot.conferences.ui.components.appbars.TopAppBar
import com.paligot.conferences.ui.screens.Screen

@Composable
fun Home(
    agendaRepository: AgendaRepository,
    userRepository: UserRepository,
    modifier: Modifier = Modifier,
    startDestination: Screen = Screen.Agenda,
    navController: NavHostController = rememberNavController(),
    onTalkClicked: (id: String) -> Unit,
    onFaqClick: (url: String) -> Unit,
    onCoCClick: (url: String) -> Unit,
    onTwitterClick: (url: String?) -> Unit,
    onLinkedInClick: (url: String?) -> Unit,
    onPartnerClick: (siteUrl: String?) -> Unit,
    onScannerClicked: () -> Unit,
    onQrCodeClicked: () -> Unit,
    onReportClicked: () -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val screen = currentDestination?.route?.getScreen() ?: startDestination
    Scaffold(
        topBar = {
            TopAppBar(
                title = screen.title,
                actions = screen.actions,
                onActionClicked = {
                    when (it) {
                        ActionItemId.QrCodeScannerActionItem -> onScannerClicked()
                        ActionItemId.QrCodeActionItem -> onQrCodeClicked()
                        ActionItemId.ReportActionItem -> onReportClicked()
                        else -> TODO()
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                selected = screen,
                onClick = {
                    navController.navigate(it.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        },
    ) {
        NavHost(navController, startDestination = Screen.Agenda.route, modifier = modifier.padding(it)) {
            composable(Screen.Agenda.route) {
                AgendaVM(
                    agendaRepository = agendaRepository,
                    onTalkClicked = onTalkClicked,
                )
            }
            composable(Screen.Networking.route) {
                NetworkingVM(userRepository = userRepository)
            }
            composable(Screen.Event.route) {
                EventVM(
                    agendaRepository = agendaRepository,
                    onFaqClick = onFaqClick,
                    onCoCClick = onCoCClick,
                    onTwitterClick = onTwitterClick,
                    onLinkedInClick = onLinkedInClick,
                    onPartnerClick = onPartnerClick
                )
            }
        }
    }
}

internal fun String.getScreen(): Screen = when (this) {
    Screen.Agenda.route -> Screen.Agenda
    Screen.Event.route -> Screen.Event
    Screen.Networking.route -> Screen.Networking
    else -> TODO()
}
