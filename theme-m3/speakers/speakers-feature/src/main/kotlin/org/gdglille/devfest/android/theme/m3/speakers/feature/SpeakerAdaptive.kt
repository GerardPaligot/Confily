package org.gdglille.devfest.android.theme.m3.speakers.feature

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SpeakerAdaptive(
    showBackInDetail: Boolean,
    onTalkClicked: (id: String) -> Unit,
    onLinkClicked: (url: String) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    modifier: Modifier = Modifier,
    state: LazyGridState = rememberLazyGridState()
) {
    val scaffoldDirective = calculatePaneScaffoldDirective(currentWindowAdaptiveInfo())
    val navigator = rememberListDetailPaneScaffoldNavigator(scaffoldDirective)
    var selectedItem: String? by rememberSaveable { mutableStateOf(null) }
    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        modifier = modifier,
        listPane = {
            AnimatedPane(Modifier) {
                SpeakersGridVM(
                    onSpeakerClicked = {
                        selectedItem = it
                        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                    },
                    state = state,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedContentScope = animatedContentScope
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
                        sharedTransitionScope = sharedTransitionScope,
                        animatedContentScope = animatedContentScope,
                        navigationIcon = if (showBackInDetail) {
                            @Composable {
                                Back {
                                    if (navigator.canNavigateBack()) {
                                        navigator.navigateBack()
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
