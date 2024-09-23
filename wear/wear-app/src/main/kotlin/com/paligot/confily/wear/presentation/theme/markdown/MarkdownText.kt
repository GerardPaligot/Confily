package com.paligot.confily.wear.presentation.theme.markdown

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.wear.compose.material3.MaterialTheme
import com.mikepenz.markdown.compose.Markdown
import com.mikepenz.markdown.model.DefaultMarkdownColors
import com.mikepenz.markdown.model.DefaultMarkdownTypography
import com.mikepenz.markdown.model.MarkdownColors
import com.mikepenz.markdown.model.MarkdownTypography

@Composable
fun MarkdownText(
    text: String,
    modifier: Modifier = Modifier,
    bodyColor: Color = MaterialTheme.colorScheme.onSurface,
    bodyTextStyle: TextStyle = MaterialTheme.typography.bodyMedium
) {
    Markdown(
        content = text,
        colors = markdownColor(bodyColor),
        typography = markdownTypography(bodyTextStyle),
        modifier = modifier
    )
}

@Composable
private fun markdownColor(
    text: Color = MaterialTheme.colorScheme.onBackground,
    codeText: Color = MaterialTheme.colorScheme.onBackground,
    inlineCodeText: Color = codeText,
    linkText: Color = text,
    codeBackground: Color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
    inlineCodeBackground: Color = codeBackground,
    dividerColor: Color = MaterialTheme.colorScheme.outlineVariant
): MarkdownColors = DefaultMarkdownColors(
    text = text,
    codeText = codeText,
    inlineCodeText = inlineCodeText,
    linkText = linkText,
    codeBackground = codeBackground,
    inlineCodeBackground = inlineCodeBackground,
    dividerColor = dividerColor
)

@Composable
private fun markdownTypography(
    h1: TextStyle = MaterialTheme.typography.displayLarge,
    h2: TextStyle = MaterialTheme.typography.displayMedium,
    h3: TextStyle = MaterialTheme.typography.displaySmall,
    h4: TextStyle = MaterialTheme.typography.titleLarge,
    h5: TextStyle = MaterialTheme.typography.titleMedium,
    h6: TextStyle = MaterialTheme.typography.titleSmall,
    text: TextStyle = MaterialTheme.typography.bodyLarge,
    code: TextStyle = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace),
    quote: TextStyle = MaterialTheme.typography.bodyMedium.plus(SpanStyle(fontStyle = FontStyle.Italic)),
    paragraph: TextStyle = MaterialTheme.typography.bodyLarge,
    ordered: TextStyle = MaterialTheme.typography.bodyLarge,
    bullet: TextStyle = MaterialTheme.typography.bodyLarge,
    list: TextStyle = MaterialTheme.typography.bodyLarge
): MarkdownTypography = DefaultMarkdownTypography(
    h1 = h1, h2 = h2, h3 = h3, h4 = h4, h5 = h5, h6 = h6,
    text = text, quote = quote, code = code, paragraph = paragraph,
    ordered = ordered, bullet = bullet, list = list
)
