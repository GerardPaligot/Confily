package org.gdglille.devfest.android.theme.m3.schedules.feature

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.SupportingPaneScaffold
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
    onScheduleStarted: () -> Unit,
    onFilterClicked: () -> Unit,
    onTalkClicked: (id: String) -> Unit,
    showFilterIcon: Boolean,
    modifier: Modifier = Modifier,
    isSmallSize: Boolean = false,
) {
    SupportingPaneScaffold(
        modifier = modifier,
        mainPane = {
            ScheduleGridVM(
                onScheduleStarted = onScheduleStarted,
                onFilterClicked = onFilterClicked,
                onTalkClicked = onTalkClicked,
                showFilterIcon = showFilterIcon,
                isSmallSize = isSmallSize
            )
        },
        supportingPane = {
            AgendaFiltersCompactVM(
                containerColor = if (showFilterIcon) MaterialTheme.colorScheme.background
                else MaterialTheme.colorScheme.surfaceContainerHigh
            )
        }
    )
}
