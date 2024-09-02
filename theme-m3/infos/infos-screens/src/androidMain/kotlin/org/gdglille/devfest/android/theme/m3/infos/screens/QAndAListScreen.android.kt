package org.gdglille.devfest.android.theme.m3.infos.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.collections.immutable.persistentListOf
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.models.ui.QuestionAndResponseUi
import org.gdglille.devfest.theme.m3.infos.screens.QAndAListScreen

@Preview
@Composable
private fun QAndAListPreview() {
    Conferences4HallTheme {
        QAndAListScreen(
            qAndA = persistentListOf(
                QuestionAndResponseUi.fake,
                QuestionAndResponseUi.fake.copy(expanded = true)
            ),
            onExpandedClicked = {},
            onLinkClicked = {}
        )
    }
}
