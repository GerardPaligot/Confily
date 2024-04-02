package org.gdglille.devfest.android.theme.m3.style.markdown

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.RichText
import com.halilibo.richtext.ui.RichTextThemeIntegration
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme

@Composable
fun MarkdownText(
    text: String,
    modifier: Modifier = Modifier,
    bodyColor: Color = MarkdownTextDefaults.bodyColor,
    bodyTextStyle: TextStyle = MarkdownTextDefaults.bodyTextStyle
) {
    RichTextThemeIntegration(
        textStyle = { bodyTextStyle },
        ProvideTextStyle = null,
        contentColor = { bodyColor },
        ProvideContentColor = null
    ) {
        RichText(modifier = modifier) {
            Markdown(text)
        }
    }
}

@Preview
@Composable
internal fun SocialsSectionPreview() {
    Conferences4HallTheme {
        MarkdownText(
            text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
        )
    }
}
