package org.gdglille.devfest.android.theme.vitamin.ui.screens.event

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.decathlon.vitamin.compose.dividers.VitaminDividers
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.gdglille.devfest.android.theme.vitamin.ui.components.qanda.QAndAItem
import org.gdglille.devfest.android.theme.vitamin.ui.theme.Conferences4HallTheme
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
            qAndA = persistentListOf(
                QuestionAndResponseUi.fake,
                QuestionAndResponseUi.fake.copy(expanded = true)
            ),
            onExpandedClicked = {},
            onLinkClicked = {}
        )
    }
}
