package org.gdglille.devfest.android.components.partners

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import org.gdglille.devfest.android.theme.Conferences4HallTheme
import org.gdglille.devfest.models.PartnerGroupUi
import org.gdglille.devfest.models.PartnerItemUi

@ExperimentalMaterial3Api
@Composable
fun PartnerRow(
    partners: ImmutableList<PartnerItemUi>,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    maxItems: Int = 3,
    onPartnerClick: (id: String) -> Unit,
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val width = (this.maxWidth - (8.dp * (maxItems - 1))) / maxItems
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            partners.forEach {
                Conferences4HallTheme(useDarkTheme = false) {
                    PartnerItem(
                        partnerUi = it,
                        modifier = Modifier.width(width).aspectRatio(1f),
                        isLoading = isLoading,
                        onClick = onPartnerClick
                    )
                }
            }
        }
    }
}

@ExperimentalMaterial3Api
@Preview
@Composable
fun PartnerRowPreview() {
    Conferences4HallTheme {
        PartnerRow(
            partners = PartnerGroupUi.fake.partners.first(),
            onPartnerClick = {}
        )
    }
}
