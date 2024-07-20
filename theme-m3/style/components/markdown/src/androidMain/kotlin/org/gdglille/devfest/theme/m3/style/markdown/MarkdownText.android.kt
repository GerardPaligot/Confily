package org.gdglille.devfest.theme.m3.style.markdown

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme

@Preview
@Composable
internal fun MarkdownTextPreview() {
    Conferences4HallTheme {
        MarkdownText(
            text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
        )
    }
}
