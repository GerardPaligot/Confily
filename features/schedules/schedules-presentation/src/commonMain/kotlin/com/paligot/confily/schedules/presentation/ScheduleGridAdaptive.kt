package com.paligot.confily.schedules.presentation

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
    FlowPreview::class
)
@Composable
fun ScheduleGridAdaptive(
    onFilterClicked: () -> Unit,
    onTalkClicked: (id: String) -> Unit,
    onEventSessionClicked: (id: String) -> Unit,
    showFilterIcon: Boolean,
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
                onFilterClicked = onFilterClicked,
                onTalkClicked = onTalkClicked,
                onEventSessionClicked = onEventSessionClicked,
                showFilterIcon = showFilterIcon,
                state = state,
                isSmallSize = isSmallSize
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
