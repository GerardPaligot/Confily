package org.gdglille.devfest.android.theme.m3.schedules.feature

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.SupportingPaneScaffold
import androidx.compose.material3.adaptive.SupportingPaneScaffoldDefaults
import androidx.compose.material3.adaptive.calculateStandardPaneScaffoldDirective
import androidx.compose.material3.adaptive.calculateSupportingPaneScaffoldState
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.gdglille.devfest.android.theme.m3.style.adaptive.calculateStandardPaneScaffoldDirectiveAdaptive
import org.gdglille.devfest.android.theme.m3.style.adaptive.windowInsetsAdaptive

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
    isSmallSize: Boolean = false
) {
    val scaffoldDirective =
        calculateStandardPaneScaffoldDirectiveAdaptive(currentWindowAdaptiveInfo())
    val scaffoldState = calculateSupportingPaneScaffoldState(scaffoldDirective)
    SupportingPaneScaffold(
        scaffoldState = scaffoldState,
        windowInsets = SupportingPaneScaffoldDefaults.windowInsetsAdaptive,
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
