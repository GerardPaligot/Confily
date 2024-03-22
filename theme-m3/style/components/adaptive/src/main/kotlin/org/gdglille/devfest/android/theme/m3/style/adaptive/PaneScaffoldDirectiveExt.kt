package org.gdglille.devfest.android.theme.m3.style.adaptive

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.HingePolicy
import androidx.compose.material3.adaptive.PaneScaffoldDirective
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.calculateStandardPaneScaffoldDirective
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * By default, adaptive scaffold configure a content padding as if it was used
 * without NavigationSuiteScaffold component and add a padding around the main pane.
 * This padding is useful when we display more than one pane on the screen but when
 * we are in compact mode, we don't want any padding.
 */
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
fun calculateStandardPaneScaffoldDirectiveAdaptive(
    windowAdaptiveInfo: WindowAdaptiveInfo,
    verticalHingePolicy: HingePolicy = HingePolicy.AvoidSeparating
): PaneScaffoldDirective {
    val directive = calculateStandardPaneScaffoldDirective(windowAdaptiveInfo, verticalHingePolicy)
    return if (windowAdaptiveInfo.windowSizeClass.widthSizeClass.isCompat) {
        directive.copy(contentPadding = PaddingValues(0.dp))
    } else {
        directive
    }
}

@Suppress("LongParameterList")
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
fun PaneScaffoldDirective.copy(
    contentPadding: PaddingValues = this.contentPadding,
    maxHorizontalPartitions: Int = this.maxHorizontalPartitions,
    horizontalPartitionSpacerSize: Dp = this.horizontalPartitionSpacerSize,
    maxVerticalPartitions: Int = this.maxVerticalPartitions,
    verticalPartitionSpacerSize: Dp = this.verticalPartitionSpacerSize,
    excludedBounds: List<Rect> = this.excludedBounds
): PaneScaffoldDirective = PaneScaffoldDirective(
    contentPadding = contentPadding,
    maxHorizontalPartitions = maxHorizontalPartitions,
    horizontalPartitionSpacerSize = horizontalPartitionSpacerSize,
    maxVerticalPartitions = maxVerticalPartitions,
    verticalPartitionSpacerSize = verticalPartitionSpacerSize,
    excludedBounds = excludedBounds
)
