package org.gdglille.devfest.android.theme.m3.partners.feature

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.theme.m3.partners.ui.partners.PartnerDivider
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.android.theme.m3.style.partners.PartnerItem
import org.gdglille.devfest.android.theme.m3.style.placeholder
import org.gdglille.devfest.models.ui.PartnerGroupsUi

@Composable
fun Partners(
    partners: PartnerGroupsUi,
    modifier: Modifier = Modifier,
    state: LazyGridState = rememberLazyGridState(),
    isLoading: Boolean = false,
    columnCount: Int = 3,
    onPartnerClick: (id: String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columnCount),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        state = state,
        modifier = modifier
    ) {
        partners.groups.forEach {
            item(span = { GridItemSpan(currentLineSpan = columnCount) }) {
                PartnerDivider(title = it.type)
            }
            items(it.partners) {
                PartnerItem(
                    url = it.logoUrl,
                    contentDescription = it.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .placeholder(visible = isLoading),
                    onClick = { onPartnerClick(it.id) }
                )
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
fun PartnersPreview() {
    Conferences4HallTheme {
        Scaffold {
            Partners(
                partners = PartnerGroupsUi.fake,
                onPartnerClick = {}
            )
        }
    }
}
