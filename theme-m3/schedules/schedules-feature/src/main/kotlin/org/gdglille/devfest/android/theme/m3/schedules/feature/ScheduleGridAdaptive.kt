package org.gdglille.devfest.android.theme.m3.schedules.feature

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffold
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation.rememberSupportingPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@OptIn(
    ExperimentalMaterial3AdaptiveApi::class,
    ExperimentalCoroutinesApi::class,
    FlowPreview::class,
    ExperimentalSharedTransitionApi::class
)
@Composable
fun ScheduleGridAdaptive(
    onScheduleStarted: () -> Unit,
    onFilterClicked: () -> Unit,
    onTalkClicked: (id: String) -> Unit,
    showFilterIcon: Boolean,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    modifier: Modifier = Modifier,
    state: LazyGridState = rememberLazyGridState(),
    isSmallSize: Boolean = false
) {
    val scaffoldDirective = calculatePaneScaffoldDirective(currentWindowAdaptiveInfo())
    val navigator = rememberSupportingPaneScaffoldNavigator(scaffoldDirective)
    SupportingPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        modifier = modifier,
        mainPane = {
            ScheduleGridVM(
                onScheduleStarted = onScheduleStarted,
                onFilterClicked = onFilterClicked,
                onTalkClicked = onTalkClicked,
                showFilterIcon = showFilterIcon,
                isSmallSize = isSmallSize,
                state = state,
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = animatedContentScope
            )
        },
        supportingPane = {
            AgendaFiltersCompactVM(
                containerColor = if (showFilterIcon) {
                    MaterialTheme.colorScheme.background
                } else {
                    MaterialTheme.colorScheme.surfaceContainerHigh
                }
            )
        }
    )
}
