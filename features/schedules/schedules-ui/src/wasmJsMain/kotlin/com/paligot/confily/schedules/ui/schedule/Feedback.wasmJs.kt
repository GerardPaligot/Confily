package com.paligot.confily.schedules.ui.schedule

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun Feedback(
    projectId: String,
    sessionId: String,
    canGiveFeedback: Boolean,
    modifier: Modifier
) {
    Text("Feedback not available on web")
}
