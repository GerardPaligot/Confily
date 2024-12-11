package com.paligot.confily.partners.presentation

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.paligot.confily.style.components.adaptive.BackHandler
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun PartnersAdaptive(
    showBackInDetail: Boolean,
    onItineraryClicked: (lat: Double, lng: Double) -> Unit,
    onLinkClicked: (url: String) -> Unit,
    modifier: Modifier = Modifier,
    state: LazyGridState = rememberLazyGridState()
) {
    val scope = rememberCoroutineScope()
    val scaffoldDirective = calculatePaneScaffoldDirective(currentWindowAdaptiveInfo())
    val navigator = rememberListDetailPaneScaffoldNavigator<String>(scaffoldDirective)
    BackHandler(navigator.canNavigateBack()) {
        scope.launch { navigator.navigateBack() }
    }
    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        modifier = modifier,
        listPane = {
            AnimatedPane {
                PartnersGridVM(
                    onPartnerClick = {
                        scope.launch {
                            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, contentKey = it)
                        }
                    },
                    state = state
                )
            }
        },
        detailPane = {
            navigator.currentDestination?.contentKey?.let {
                AnimatedPane {
                    PartnerDetailVM(
                        partnerId = it,
                        onLinkClicked = onLinkClicked,
                        onItineraryClicked = onItineraryClicked,
                        navigationIcon = if (showBackInDetail) {
                            @Composable {
                                Back {
                                    if (navigator.canNavigateBack()) {
                                        scope.launch { navigator.navigateBack() }
                                    }
                                }
                            }
                        } else {
                            null
                        }
                    )
                }
            }
        }
    )
}
