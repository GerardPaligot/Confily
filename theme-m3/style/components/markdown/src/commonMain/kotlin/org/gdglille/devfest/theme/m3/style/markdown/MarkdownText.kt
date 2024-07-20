package org.gdglille.devfest.theme.m3.style.markdown

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.mikepenz.markdown.compose.Markdown
import com.mikepenz.markdown.m3.markdownColor
import com.mikepenz.markdown.m3.markdownTypography

@Composable
fun MarkdownText(
    text: String,
    modifier: Modifier = Modifier,
    bodyColor: Color = MarkdownTextDefaults.bodyColor,
    bodyTextStyle: TextStyle = MarkdownTextDefaults.bodyTextStyle
) {
    Markdown(
        content = text,
        colors = markdownColor(bodyColor),
        typography = markdownTypography(bodyTextStyle),
        modifier = modifier
    )
}
