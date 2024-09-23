package com.paligot.confily.wear.presentation.events.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.FilledTonalButton
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.ScrollIndicator
import androidx.wear.compose.material3.Text
import com.paligot.confily.wear.R

@Composable
fun EventPane(
    modelUi: EventModelUi,
    onSchedulesClick: () -> Unit,
    onSpeakersClick: () -> Unit,
    onPartnersClick: () -> Unit,
    onChangeEventClick: () -> Unit,
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
            item { ListHeader { Text(modelUi.name) } }
            item {
                FilledTonalButton(
                    label = { Text(stringResource(R.string.action_schedules)) },
                    onClick = onSchedulesClick,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                FilledTonalButton(
                    label = { Text(stringResource(R.string.action_speakers)) },
                    onClick = onSpeakersClick,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                FilledTonalButton(
                    label = { Text(stringResource(R.string.action_partners)) },
                    onClick = onPartnersClick,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                FilledTonalButton(
                    label = { Text(stringResource(R.string.action_change_event)) },
                    onClick = onChangeEventClick,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
