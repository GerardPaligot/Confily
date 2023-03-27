package org.gdglille.devfest.android.theme.vitamin.ui.components.talks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decathlon.vitamin.compose.foundation.VitaminTheme
import org.gdglille.devfest.android.theme.vitamin.ui.theme.Conferences4HallTheme
import org.gdglille.devfest.android.ui.resources.R
import com.decathlon.vitamin.compose.foundation.R as RVitamin

@Composable
fun OpenFeedbackNotStarted(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(VitaminTheme.colors.vtmnBackgroundTertiary)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 56.dp, horizontal = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CompositionLocalProvider(LocalContentColor provides VitaminTheme.colors.vtmnContentTertiary) {
                Row {
                    Icon(
                        painter = painterResource(RVitamin.drawable.ic_vtmn_emotion_laugh_line),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                    Icon(
                        painter = painterResource(RVitamin.drawable.ic_vtmn_emotion_line),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                    Icon(
                        painter = painterResource(RVitamin.drawable.ic_vtmn_emotion_normal_line),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                    Icon(
                        painter = painterResource(RVitamin.drawable.ic_vtmn_emotion_unhappy_line),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                    Icon(
                        painter = painterResource(RVitamin.drawable.ic_vtmn_emotion_sad_line),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            CompositionLocalProvider(LocalContentColor provides VitaminTheme.colors.vtmnContentSecondary) {
                Text(
                    text = stringResource(R.string.text_openfeedback_not_started_title),
                    style = VitaminTheme.typography.subtitle1
                )
                Text(
                    text = stringResource(R.string.text_openfeedback_not_started),
                    style = VitaminTheme.typography.body3,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview
@Composable
fun OpenFeedbackNotStartedPreview() {
    Conferences4HallTheme {
        OpenFeedbackNotStarted()
    }
}
