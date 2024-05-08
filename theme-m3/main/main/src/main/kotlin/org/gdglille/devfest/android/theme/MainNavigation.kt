package org.gdglille.devfest.android.theme

import android.content.res.Configuration
import androidx.compose.material3.Icon
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowSize
import androidx.compose.material3.adaptive.navigationsuite.ExperimentalMaterial3AdaptiveNavigationSuiteApi
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import io.openfeedback.viewmodels.OpenFeedbackFirebaseConfig
import org.gdglille.devfest.android.theme.m3.events.feature.EventListVM
import org.gdglille.devfest.android.theme.m3.infos.feature.InfoCompactVM
import org.gdglille.devfest.android.theme.m3.infos.feature.TicketQrCodeScanner
import org.gdglille.devfest.android.theme.m3.navigation.Screen
import org.gdglille.devfest.android.theme.m3.networking.feature.NetworkingCompactVM
import org.gdglille.devfest.android.theme.m3.networking.feature.ProfileInputVM
import org.gdglille.devfest.android.theme.m3.networking.feature.VCardQrCodeScanner
import org.gdglille.devfest.android.theme.m3.partners.feature.PartnerDetailVM
import org.gdglille.devfest.android.theme.m3.partners.feature.PartnersAdaptive
import org.gdglille.devfest.android.theme.m3.schedules.feature.AgendaFiltersCompactVM
import org.gdglille.devfest.android.theme.m3.schedules.feature.ScheduleDetailEventSessionVM
import org.gdglille.devfest.android.theme.m3.schedules.feature.ScheduleDetailOrientableVM
import org.gdglille.devfest.android.theme.m3.schedules.feature.ScheduleGridAdaptive
import org.gdglille.devfest.android.theme.m3.speakers.feature.SpeakerAdaptive
import org.gdglille.devfest.android.theme.m3.speakers.feature.SpeakerDetailVM
import org.gdglille.devfest.android.theme.m3.style.adaptive.isCompat
import org.gdglille.devfest.android.theme.m3.style.adaptive.isMedium
import org.gdglille.devfest.android.theme.m3.style.appbars.iconColor
import org.gdglille.devfest.models.ui.ExportNetworkingUi
import org.gdglille.devfest.models.ui.VCardModel
import org.gdglille.devfest.models.ui.convertToModelUi
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.koinViewModel

@Suppress("LongMethod")
@OptIn(
    ExperimentalMaterial3AdaptiveNavigationSuiteApi::class,
    ExperimentalMaterial3AdaptiveApi::class,
    ExperimentalResourceApi::class,
    ExperimentalComposeUiApi::class,
    ExperimentalMaterial3WindowSizeClassApi::class
)
@Composable
fun MainNavigation(
    startDestination: String,
    openfeedbackFirebaseConfig: OpenFeedbackFirebaseConfig,
    launchUrl: (String) -> Unit,
    onContactExportClicked: (ExportNetworkingUi) -> Unit,
    onReportByPhoneClicked: (String) -> Unit,
    onReportByEmailClicked: (String) -> Unit,
    onShareClicked: (text: String) -> Unit,
    onItineraryClicked: (lat: Double, lng: Double) -> Unit,
    onScheduleStarted: () -> Unit,
    onProfileCreated: () -> Unit,
    modifier: Modifier = Modifier,
    savedStateHandle: SavedStateHandle? = null,
    navController: NavHostController = rememberNavController(),
    viewModel: MainNavigationViewModel = koinViewModel()
) {
    if (savedStateHandle != null) {
        val qrCodeTicket by savedStateHandle.getLiveData<String>(ResultKey.QR_CODE_TICKET)
            .observeAsState()
        val qrCodeVCard by savedStateHandle.getLiveData<VCardModel>(ResultKey.QR_CODE_VCARD)
            .observeAsState()
        LaunchedEffect(qrCodeTicket, qrCodeVCard) {
            qrCodeTicket?.let {
                viewModel.saveTicket(it)
                savedStateHandle.remove<String>(ResultKey.QR_CODE_TICKET)
            }
            qrCodeVCard?.let {
                viewModel.saveNetworkingProfile(it.convertToModelUi())
                savedStateHandle.remove<String>(ResultKey.QR_CODE_VCARD)
            }
        }
    }
    val rootUri = "c4h://event"
    val config = LocalConfiguration.current
    val uiState = viewModel.uiState.collectAsState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val route = currentDestination?.route ?: Screen.ScheduleList.route
    val windowSize = with(LocalDensity.current) { currentWindowSize().toSize().toDpSize() }
    val adaptiveInfo = WindowSizeClass.calculateFromSize(windowSize)
    val heightCompact = adaptiveInfo.heightSizeClass.isCompat
    val layoutType = if (heightCompact) {
        NavigationSuiteType.NavigationRail
    } else {
        NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(currentWindowAdaptiveInfo())
    }
    NavigationSuiteScaffold(
        layoutType = layoutType,
        modifier = modifier.semantics {
            testTagsAsResourceId = true
        },
        navigationSuiteItems = {
            when (uiState.value) {
                is MainNavigationUiState.Success -> {
                    val navActions = (uiState.value as MainNavigationUiState.Success).navActions
                    navActions.actions.forEach { action ->
                        val selected = action.route == route
                        item(
                            selected = selected,
                            icon = {
                                Icon(
                                    imageVector = if (selected) action.iconSelected else action.icon,
                                    contentDescription = stringResource(action.label),
                                    tint = iconColor(selected = selected)
                                )
                            },
                            onClick = {
                                navController.navigate(action.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }

                else -> {}
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            builder = {
                composable(route = Screen.EventList.route) {
                    EventListVM(
                        onEventClicked = {
                            navController.navigate(Screen.ScheduleList.route) {
                                this.popUpTo(Screen.EventList.route) {
                                    inclusive = true
                                }
                            }
                        }
                    )
                }
                composable(Screen.ScheduleList.route) {
                    val showFilterIcon = adaptiveInfo.widthSizeClass.isCompat ||
                        (adaptiveInfo.widthSizeClass.isMedium && config.isPortrait)
                    val isSmallSize = adaptiveInfo.heightSizeClass.isCompat
                    ScheduleGridAdaptive(
                        onScheduleStarted = onScheduleStarted,
                        onFilterClicked = { navController.navigate(Screen.ScheduleFilters.route) },
                        onTalkClicked = { navController.navigate(Screen.Schedule.route(it)) },
                        onEventSessionClicked = { navController.navigate(Screen.ScheduleEvent.route(it)) },
                        showFilterIcon = showFilterIcon,
                        isSmallSize = isSmallSize
                    )
                }
                composable(route = Screen.ScheduleFilters.route) {
                    AgendaFiltersCompactVM(
                        navigationIcon = { Back { navController.popBackStack() } }
                    )
                }
                composable(
                    route = Screen.Schedule.route,
                    arguments = listOf(navArgument("scheduleId") { type = NavType.StringType }),
                    deepLinks = listOf(
                        navDeepLink {
                            uriPattern = "$rootUri/${Screen.Schedule.route}"
                        }
                    )
                ) {
                    ScheduleDetailOrientableVM(
                        scheduleId = it.arguments?.getString("scheduleId")!!,
                        openfeedbackFirebaseConfig = openfeedbackFirebaseConfig,
                        onBackClicked = { navController.popBackStack() },
                        onSpeakerClicked = { navController.navigate(Screen.Speaker.route(it)) },
                        onShareClicked = onShareClicked
                    )
                }
                composable(
                    route = Screen.ScheduleEvent.route,
                    arguments = listOf(navArgument("scheduleId") { type = NavType.StringType })
                ) {
                    ScheduleDetailEventSessionVM(
                        scheduleId = it.arguments?.getString("scheduleId")!!,
                        onBackClicked = { navController.popBackStack() }
                    )
                }
                composable(Screen.SpeakerList.route) {
                    SpeakerAdaptive(
                        showBackInDetail = adaptiveInfo.widthSizeClass.isCompat,
                        onTalkClicked = { navController.navigate(Screen.Schedule.route(it)) },
                        onLinkClicked = { launchUrl(it) }
                    )
                }
                composable(
                    route = Screen.Speaker.route,
                    arguments = listOf(navArgument("speakerId") { type = NavType.StringType })
                ) {
                    SpeakerDetailVM(
                        speakerId = it.arguments?.getString("speakerId")!!,
                        onTalkClicked = { navController.navigate(Screen.Schedule.route(it)) },
                        onLinkClicked = { launchUrl(it) },
                        navigationIcon = { Back { navController.popBackStack() } },
                        isLandscape = config.orientation == Configuration.ORIENTATION_LANDSCAPE
                    )
                }
                composable(
                    route = Screen.MyProfile.route,
                    deepLinks = listOf(
                        navDeepLink {
                            uriPattern = "$rootUri/${Screen.MyProfile.route}"
                        }
                    )
                ) {
                    NetworkingCompactVM(
                        onCreateProfileClicked = { navController.navigate(Screen.NewProfile.route) },
                        onContactScannerClicked = { navController.navigate(Screen.ScannerVCard.route) },
                        onContactExportClicked = onContactExportClicked
                    )
                }
                composable(
                    route = Screen.NewProfile.route,
                    deepLinks = listOf(
                        navDeepLink {
                            uriPattern = "$rootUri/${Screen.NewProfile.route}"
                        }
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
                composable(Screen.PartnerList.route) {
                    PartnersAdaptive(
                        showBackInDetail = adaptiveInfo.widthSizeClass.isCompat,
                        onItineraryClicked = onItineraryClicked,
                        onLinkClicked = { launchUrl(it) }
                    )
                }
                composable(
                    route = Screen.Partner.route,
                    arguments = listOf(navArgument("partnerId") { type = NavType.StringType }),
                    deepLinks = listOf(
                        navDeepLink {
                            uriPattern = "$rootUri/${Screen.Partner.route}"
                        }
                    )
                ) {
                    PartnerDetailVM(
                        partnerId = it.arguments?.getString("partnerId")!!,
                        onLinkClicked = { launchUrl(it) },
                        onItineraryClicked = onItineraryClicked,
                        navigationIcon = { Back { navController.popBackStack() } },
                        isLandscape = config.orientation == Configuration.ORIENTATION_LANDSCAPE
                    )
                }
                composable(Screen.Event.route) {
                    InfoCompactVM(
                        onItineraryClicked = onItineraryClicked,
                        onLinkClicked = { url -> url?.let { launchUrl(it) } },
                        onTicketScannerClicked = { navController.navigate(Screen.ScannerTicket.route) },
                        onDisconnectedClicked = {
                            navController.navigate(Screen.EventList.route) {
                                popUpTo(Screen.ScheduleList.route) {
                                    inclusive = true
                                }
                            }
                        },
                        onReportByPhoneClicked = onReportByPhoneClicked,
                        onReportByEmailClicked = onReportByEmailClicked
                    )
                }
                composable(route = Screen.ScannerVCard.route) {
                    VCardQrCodeScanner(
                        navigateToSettingsScreen = {},
                        onQrCodeDetected = { vcard ->
                            navController
                                .previousBackStackEntry
                                ?.savedStateHandle
                                ?.set(ResultKey.QR_CODE_VCARD, vcard)
                            navController.popBackStack()
                        },
                        onBackClicked = { navController.popBackStack() }
                    )
                }
                composable(route = Screen.ScannerTicket.route) {
                    TicketQrCodeScanner(
                        navigateToSettingsScreen = {},
                        onQrCodeDetected = { barcode ->
                            navController
                                .previousBackStackEntry
                                ?.savedStateHandle
                                ?.set(ResultKey.QR_CODE_TICKET, barcode)
                            navController.popBackStack()
                        },
                        onBackClicked = { navController.popBackStack() }
                    )
                }
            }
        )
    }
}
