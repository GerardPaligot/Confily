package org.gdglille.devfest.android.components.talks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.RichText
import com.halilibo.richtext.ui.RichTextThemeIntegration
import org.gdglille.devfest.android.ui.resources.R

@Composable
fun TalkAbstract(
    abstract: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground,
    subtitleTextStyle: TextStyle = MaterialTheme.typography.titleLarge,
    bodyTextStyle: TextStyle = MaterialTheme.typography.bodyMedium
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(id = R.string.screen_schedule_detail),
            color = color,
            style = subtitleTextStyle
        )
        RichTextThemeIntegration(
            textStyle = { bodyTextStyle },
            ProvideTextStyle = null,
            contentColor = { color.copy(alpha = .73f) },
            ProvideContentColor = null,
        ) {
            RichText(
                modifier = Modifier.clearAndSetSemantics {
                    contentDescription = abstract
                }
            ) {
                Markdown(abstract)
            }
        }
    }
}
