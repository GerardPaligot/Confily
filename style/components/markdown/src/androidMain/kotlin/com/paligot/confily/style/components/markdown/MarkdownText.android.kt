package com.paligot.confily.style.components.markdown

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.style.theme.ConfilyTheme

@Preview
@Composable
internal fun MarkdownTextPreview() {
    ConfilyTheme {
        MarkdownText(
            text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
        )
    }
}
