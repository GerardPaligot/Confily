package org.gdglille.devfest.android.theme.vitamin.ui.screens.partners

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.theme.vitamin.ui.components.partners.PartnerDivider
import org.gdglille.devfest.android.theme.vitamin.ui.components.partners.PartnerRow
import org.gdglille.devfest.models.PartnerGroupsUi

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Partners(
    partners: PartnerGroupsUi,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    onPartnerClick: (siteUrl: String?) -> Unit
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 8.dp),
        contentPadding = PaddingValues(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        partners.map.entries.forEach { entry ->
            item { PartnerDivider(title = entry.key) }
            items(entry.value) {
                PartnerRow(partners = it, onPartnerClick = onPartnerClick, isLoading = isLoading)
            }
        }
    }
}
