package org.gdglille.devfest.android.screens.event

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.RichText
import com.halilibo.richtext.ui.RichTextThemeIntegration
import org.gdglille.devfest.android.theme.m3.ui.R

@Composable
fun CoC(
    coc: String,
    modifier: Modifier = Modifier,
    onReportClicked: () -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 24.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = stringResource(R.string.screen_coc),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
        item {
            RichTextThemeIntegration(
                textStyle = { MaterialTheme.typography.bodyMedium },
                ProvideTextStyle = null,
                contentColor = { MaterialTheme.colorScheme.onBackground },
                ProvideContentColor = null,
            ) {
                RichText {
                    Markdown(coc)
                }
            }
        }
        item {
            Button(
                onClick = onReportClicked,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.action_contact_organizers))
            }
        }
    }
}
