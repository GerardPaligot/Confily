package org.gdglille.devfest.android.screens.event

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.gdglille.devfest.android.components.qanda.QAndAItem
import org.gdglille.devfest.android.theme.Conferences4HallTheme
import org.gdglille.devfest.models.QuestionAndResponseUi

@Composable
fun QAndAList(
    qAndA: ImmutableList<QuestionAndResponseUi>,
    onExpandedClicked: (QuestionAndResponseUi) -> Unit,
    onLinkClicked: (url: String) -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
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
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.background,
                        thickness = 1.dp
                    )
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
            qAndA = persistentListOf(
                QuestionAndResponseUi.fake,
                QuestionAndResponseUi.fake.copy(expanded = true)
            ),
            onExpandedClicked = {},
            onLinkClicked = {}
        )
    }
}
