package com.paligot.confily.infos.ui.qanda

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.models.ui.QuestionAndResponseUi
import com.paligot.confily.style.theme.ConfilyTheme

@Preview
@Composable
private fun QAndAItemPreview() {
    ConfilyTheme {
        QAndAItem(
            qAndA = QuestionAndResponseUi.fake,
            onExpandedClicked = {},
            onLinkClicked = {}
        )
    }
}

@Preview
@Composable
private fun QAndAItemExpandedPreview() {
    ConfilyTheme {
        QAndAItem(
            qAndA = QuestionAndResponseUi.fake.copy(expanded = true),
            onExpandedClicked = {},
            onLinkClicked = {}
        )
    }
}
