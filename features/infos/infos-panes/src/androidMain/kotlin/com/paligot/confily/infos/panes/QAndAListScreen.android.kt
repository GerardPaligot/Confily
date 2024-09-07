package com.paligot.confily.infos.panes

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.models.ui.QuestionAndResponseUi
import com.paligot.confily.style.theme.ConfilyTheme
import kotlinx.collections.immutable.persistentListOf

@Preview
@Composable
private fun QAndAListPreview() {
    ConfilyTheme {
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
