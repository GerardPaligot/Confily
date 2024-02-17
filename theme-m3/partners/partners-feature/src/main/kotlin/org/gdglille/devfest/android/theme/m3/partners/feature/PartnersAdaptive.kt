package org.gdglille.devfest.android.theme.m3.partners.feature

import androidx.compose.material3.adaptive.AnimatedPane
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun PartnersAdaptive(
    showBackInDetail: Boolean,
    onItineraryClicked: (lat: Double, lng: Double) -> Unit,
    onLinkClicked: (url: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val navigator = rememberListDetailPaneScaffoldNavigator()
    var selectedItem: String? by rememberSaveable { mutableStateOf(null) }
    ListDetailPaneScaffold(
        scaffoldState = navigator.scaffoldState,
        modifier = modifier,
        listPane = {
            AnimatedPane(Modifier) {
                PartnersGridVM(
                    onPartnerClick = {
                        selectedItem = it
                        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                    }
                )
            }
        },
        detailPane = {
            AnimatedPane(modifier = Modifier) {
                selectedItem?.let { item ->
                    PartnerDetailVM(
                        partnerId = item,
                        onLinkClicked = onLinkClicked,
                        onItineraryClicked = onItineraryClicked,
                        navigationIcon = if (showBackInDetail) {
                            @Composable { Back {
                                if (navigator.canNavigateBack()) {
                                    navigator.navigateBack()
                                }
                            } }
                        } else null
                    )
                }
            }
        }
    )
}
