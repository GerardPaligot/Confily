package com.paligot.confily.mapper.detail.panes

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdsClick
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Loupe
import androidx.compose.material.icons.filled.Polyline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupPositionProvider
import com.paligot.confily.mapper.detail.ui.ShortcutContainer
import com.paligot.confily.mapper.detail.ui.ShortcutIcon
import com.paligot.confily.mapper.detail.ui.models.MappingMode
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShortcutContent(
    selected: MappingMode?,
    modifier: Modifier = Modifier,
    onClick: (MappingMode) -> Unit
) {
    val scope = rememberCoroutineScope()
    LazyColumn(modifier = modifier) {
        items(MappingMode.entries) {
            val tooltipState = rememberTooltipState(isPersistent = true)
            TooltipBox(
                positionProvider = rememberPlainTooltipPositionProvider(),
                state = tooltipState,
                tooltip = { PlainTooltip { Text(text = it.name) } },
                content = {
                    ShortcutContainer(
                        onHover = {
                            scope.launch {
                                if (it) {
                                    tooltipState.show()
                                } else {
                                    tooltipState.dismiss()
                                }
                            }
                        }
                    ) {
                        ShortcutIcon(
                            painter = rememberVectorPainter(it.toIcon()),
                            selected = selected == it,
                            onClick = { onClick(it) }
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun rememberPlainTooltipPositionProvider(
    spacingBetweenTooltipAndAnchor: Dp = 4.dp
): PopupPositionProvider {
    val tooltipAnchorSpacing =
        with(LocalDensity.current) { spacingBetweenTooltipAndAnchor.roundToPx() }
    return remember(tooltipAnchorSpacing) {
        object : PopupPositionProvider {
            override fun calculatePosition(
                anchorBounds: IntRect,
                windowSize: IntSize,
                layoutDirection: LayoutDirection,
                popupContentSize: IntSize
            ): IntOffset {
                val x = anchorBounds.right + tooltipAnchorSpacing
                val y = anchorBounds.top + (anchorBounds.height / 2) - (popupContentSize.height / 2)
                return IntOffset(x, y)
            }
        }
    }
}

fun MappingMode.toIcon(): ImageVector = when (this) {
    MappingMode.Selection -> Icons.Default.AdsClick
    MappingMode.Draw -> Icons.Default.Polyline
    MappingMode.Suppression -> Icons.Default.Delete
    MappingMode.Pictogram -> Icons.Default.Loupe
}
