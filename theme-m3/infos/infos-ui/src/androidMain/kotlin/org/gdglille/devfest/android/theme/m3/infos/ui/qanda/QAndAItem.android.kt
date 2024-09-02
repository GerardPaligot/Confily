package org.gdglille.devfest.android.theme.m3.infos.ui.qanda

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.models.ui.QuestionAndResponseUi
import org.gdglille.devfest.theme.m3.infos.ui.qanda.QAndAItem

@Preview
@Composable
private fun QAndAItemPreview() {
    Conferences4HallTheme {
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
    Conferences4HallTheme {
        QAndAItem(
            qAndA = QuestionAndResponseUi.fake.copy(expanded = true),
            onExpandedClicked = {},
            onLinkClicked = {}
        )
    }
}
