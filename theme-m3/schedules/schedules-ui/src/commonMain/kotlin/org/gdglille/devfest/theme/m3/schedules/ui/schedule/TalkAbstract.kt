package org.gdglille.devfest.theme.m3.schedules.ui.schedule

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
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.title_schedule_detail
import com.paligot.confily.style.components.markdown.MarkdownText
import org.jetbrains.compose.resources.stringResource

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
            text = stringResource(Resource.string.title_schedule_detail),
            color = color,
            style = subtitleTextStyle
        )
        MarkdownText(
            text = abstract,
            bodyColor = color.copy(alpha = .73f),
            bodyTextStyle = bodyTextStyle,
            modifier = Modifier.clearAndSetSemantics {
                contentDescription = abstract
            }
        )
    }
}
