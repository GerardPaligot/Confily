package org.gdglille.devfest.android.components.talks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import io.openfeedback.android.OpenFeedbackConfig
import io.openfeedback.android.m3.OpenFeedback
import org.gdglille.devfest.android.theme.m3.ui.R
import java.util.Locale

@Composable
fun OpenFeedbackSection(
    openFeedbackSessionId: String?,
    canGiveFeedback: Boolean,
    openFeedbackState: OpenFeedbackConfig,
    modifier: Modifier = Modifier,
    subtitleTextStyle: TextStyle = MaterialTheme.typography.titleLarge,
) {
    if (!canGiveFeedback) {
        OpenFeedbackNotStarted(modifier = modifier)
    } else if (openFeedbackSessionId != null) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier
        ) {
            Text(
                text = stringResource(R.string.text_openfeedback_title),
                style = subtitleTextStyle
            )
            OpenFeedback(
                openFeedbackState = openFeedbackState,
                sessionId = openFeedbackSessionId,
                language = Locale.getDefault().language
            )
        }
    }
}
