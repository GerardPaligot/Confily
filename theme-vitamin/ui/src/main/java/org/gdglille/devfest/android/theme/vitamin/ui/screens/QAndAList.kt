package org.gdglille.devfest.android.theme.vitamin.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.decathlon.vitamin.compose.dividers.VitaminDividers
import org.gdglille.devfest.android.theme.vitamin.ui.components.qanda.QAndAItem
import org.gdglille.devfest.android.theme.vitamin.ui.theme.Conferences4HallTheme
import org.gdglille.devfest.models.QuestionAndResponseUi

@Composable
fun QAndAList(
    qAndA: List<QuestionAndResponseUi>,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    onExpandedClicked: (QuestionAndResponseUi) -> Unit,
    onLinkClicked: (url: String) -> Unit
) {
    LazyColumn(
        modifier = modifier
    ) {
        itemsIndexed(qAndA) { index, item ->
            Column {
                QAndAItem(
                    qAndA = item,
                    isLoading = isLoading,
                    onExpandedClicked = onExpandedClicked,
                    onLinkClicked = onLinkClicked
                )
                if (index != qAndA.size - 1) {
                    VitaminDividers.FullBleed(modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}

@Preview
@Composable
fun QAndAListPreview() {
    Conferences4HallTheme {
        QAndAList(
            qAndA = arrayListOf(
                QuestionAndResponseUi.fake,
                QuestionAndResponseUi.fake.copy(expanded = true)
            ),
            onExpandedClicked = {},
            onLinkClicked = {}
        )
    }
}
