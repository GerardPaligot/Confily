package org.gdglille.devfest.android.components.appbars

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.material3.TabRowDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.theme.Conferences4HallTheme

@Composable
fun ScrollableTabRow(
    modifier: Modifier = Modifier,
    containerColor: Color = TabRowDefaults.containerColor,
    contentColor: Color = LocalContentColor.current,
    edgePadding: Dp = ScrollableTabRowPadding,
    tabs: @Composable RowScope.() -> Unit
) {
    Surface(
        modifier = modifier,
        color = containerColor,
        contentColor = contentColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(align = Alignment.CenterStart)
                .horizontalScroll(rememberScrollState())
                .padding(start = edgePadding),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            content = tabs
        )
    }
}

private val ScrollableTabRowPadding = 52.dp

@Preview
@Composable
fun ScrollableTabRowPreview() {
    Conferences4HallTheme {
        ScrollableTabRow {
            Tab(text = "10 June", selected = true, onClick = { /*TODO*/ })
            Tab(text = "11 June", selected = false, onClick = { /*TODO*/ })
        }
    }
}
