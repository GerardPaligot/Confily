package com.paligot.confily.wear.theme.errors

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.CircularProgressIndicator
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import com.paligot.confily.wear.resources.R

@Composable
fun NoEventLoaded(modifier: Modifier = Modifier) {
    ScreenScaffold(modifier = modifier) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            CircularProgressIndicator()
            Text(stringResource(R.string.text_no_event_loaded))
        }
    }
}
