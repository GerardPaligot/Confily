package com.paligot.confily.schedules.ui.schedule

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.action_give_feedback
import com.paligot.confily.schedules.semantics.SchedulesSemantics
import org.jetbrains.compose.resources.stringResource

@Composable
fun GiveFeedbackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .testTag(SchedulesSemantics.giveFeedbackButton)
    ) {
        Text(text = stringResource(Resource.string.action_give_feedback))
    }
}
