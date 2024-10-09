package com.paligot.confily.wear.events.panes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.ListSubheader
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.ScrollIndicator
import androidx.wear.compose.material3.Text
import com.paligot.confily.wear.resources.R

@Composable
fun MenusPane(
    modelUi: MenusModelUi,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScalingLazyListState()
    ScreenScaffold(
        scrollState = scrollState,
        modifier = modifier,
        scrollIndicator = {
            ScrollIndicator(scrollState, modifier = Modifier.align(Alignment.CenterEnd))
        }
    ) {
        ScalingLazyColumn(
            contentPadding = PaddingValues(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            item { ListHeader { Text(stringResource(R.string.screen_menus)) } }
            items(modelUi.menus) { menu ->
                Column(modifier = Modifier.fillMaxWidth()) {
                    ListSubheader { Text(menu.name) }
                    Text(menu.dish)
                    Text(menu.accompaniment)
                    Text(menu.dessert)
                }
            }
            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}
