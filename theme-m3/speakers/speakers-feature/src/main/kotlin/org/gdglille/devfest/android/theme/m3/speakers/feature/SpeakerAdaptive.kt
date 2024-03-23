package org.gdglille.devfest.android.theme.m3.speakers.feature

import androidx.activity.compose.BackHandler
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
fun SpeakerAdaptive(
    showBackInDetail: Boolean,
    onTalkClicked: (id: String) -> Unit,
    onLinkClicked: (url: String) -> Unit,
    modifier: Modifier = Modifier
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
                SpeakersGridVM(
                    onSpeakerClicked = {
                        selectedItem = it
                        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                    }
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
                    SpeakerDetailVM(
                        speakerId = item,
                        onTalkClicked = onTalkClicked,
                        onLinkClicked = onLinkClicked,
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
