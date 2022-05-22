package org.gdglille.devfest.android.screens.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.gdglille.devfest.android.components.appbars.ActionItemId
import org.gdglille.devfest.android.components.appbars.BottomAppBar
import org.gdglille.devfest.android.components.appbars.TopAppBar
import org.gdglille.devfest.android.screens.Screen
import org.gdglille.devfest.android.screens.agenda.AgendaVM
import org.gdglille.devfest.android.screens.event.EventVM
import org.gdglille.devfest.android.screens.partners.PartnersVM
import org.gdglille.devfest.android.screens.users.NetworkingVM
import org.gdglille.devfest.repositories.AgendaRepository
import org.gdglille.devfest.repositories.UserRepository

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
    onTicketScannerClicked: () -> Unit,
    onMenusClicked: () -> Unit,
    onQrCodeClicked: () -> Unit,
    onReportClicked: () -> Unit
) {
    val viewModel: HomeViewModel = viewModel(
        factory = HomeViewModel.Factory.create(agendaRepository)
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val screen = currentDestination?.route?.getScreen() ?: startDestination
    Scaffold(
        topBar = {
            TopAppBar(
                title = stringResource(id = screen.title),
                actions = screen.actions,
                onActionClicked = {
                    when (it) {
                        ActionItemId.VCardQrCodeScannerActionItem -> onScannerClicked()
                        ActionItemId.QrCodeActionItem -> onQrCodeClicked()
                        ActionItemId.ReportActionItem -> onReportClicked()
                        ActionItemId.FavoriteSchedulesActionItem -> {
                            viewModel.toggleFavoriteFiltering()
                        }
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
        }
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
                    onTicketScannerClicked = onTicketScannerClicked,
                    onTwitterClick = onTwitterClick,
                    onLinkedInClick = onLinkedInClick,
                    onMenusClicked = onMenusClicked
                )
            }
            composable(Screen.Partners.route) {
                PartnersVM(
                    agendaRepository = agendaRepository,
                    onPartnerClick = onPartnerClick
                )
            }
        }
    }
}

internal fun String.getScreen(): Screen = when (this) {
    Screen.Agenda.route -> Screen.Agenda
    Screen.Networking.route -> Screen.Networking
    Screen.Partners.route -> Screen.Partners
    Screen.Event.route -> Screen.Event
    else -> TODO()
}
