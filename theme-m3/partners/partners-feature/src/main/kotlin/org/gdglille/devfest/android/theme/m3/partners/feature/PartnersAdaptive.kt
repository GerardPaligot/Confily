package org.gdglille.devfest.android.theme.m3.partners.feature

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.adaptive.AnimatedPane
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.ListDetailPaneScaffoldDefaults
import androidx.compose.material3.adaptive.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.gdglille.devfest.android.theme.m3.style.adaptive.calculateStandardPaneScaffoldDirectiveAdaptive
import org.gdglille.devfest.android.theme.m3.style.adaptive.windowInsetsAdaptive

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun PartnersAdaptive(
    showBackInDetail: Boolean,
    onItineraryClicked: (lat: Double, lng: Double) -> Unit,
    onLinkClicked: (url: String) -> Unit,
    modifier: Modifier = Modifier,
    state: LazyGridState = rememberLazyGridState()
) {
    val scaffoldDirective =
        calculateStandardPaneScaffoldDirectiveAdaptive(currentWindowAdaptiveInfo())
    val navigator = rememberListDetailPaneScaffoldNavigator(scaffoldDirective)
    var selectedItem: String? by rememberSaveable { mutableStateOf(null) }
    ListDetailPaneScaffold(
        scaffoldState = navigator.scaffoldState,
        windowInsets = ListDetailPaneScaffoldDefaults.windowInsetsAdaptive,
        modifier = modifier,
        listPane = {
            AnimatedPane(Modifier) {
                PartnersGridVM(
                    onPartnerClick = {
                        selectedItem = it
                        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                    },
                    state = state
                )
            }
        },
        detailPane = {
            AnimatedPane(modifier = Modifier) {
                selectedItem?.let { item ->
                    BackHandler {
                        if (navigator.canNavigateBack()) {
                            navigator.navigateBack()
                        }
                    }
                    PartnerDetailVM(
                        partnerId = item,
                        onLinkClicked = onLinkClicked,
                        onItineraryClicked = onItineraryClicked,
                        navigationIcon = if (showBackInDetail) {
                            @Composable {
                                Back {
                                    if (navigator.canNavigateBack()) {
                                        navigator.navigateBack()
                                    }
                                }
                            }
                        } else null
                    )
                }
            }
        }
    )
}
