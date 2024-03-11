package org.gdglille.devfest.android.theme.m3.schedules.ui.schedule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.RichText
import com.halilibo.richtext.ui.RichTextThemeIntegration
import org.gdglille.devfest.android.shared.resources.Resource
import org.gdglille.devfest.android.shared.resources.screen_schedule_detail
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalResourceApi::class)
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
            text = stringResource(Resource.string.screen_schedule_detail),
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
