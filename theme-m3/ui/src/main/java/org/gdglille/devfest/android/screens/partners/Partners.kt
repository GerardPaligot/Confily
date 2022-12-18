package org.gdglille.devfest.android.screens.partners

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.components.partners.PartnerDivider
import org.gdglille.devfest.android.components.partners.PartnerRow
import org.gdglille.devfest.models.PartnerGroupsUi

@ExperimentalMaterial3Api
@Composable
fun Partners(
    partners: PartnerGroupsUi,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    onPartnerClick: (id: String) -> Unit
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 8.dp),
        contentPadding = PaddingValues(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        partners.groups.forEach {
            item { PartnerDivider(title = it.type) }
            items(it.partners) {
                PartnerRow(partners = it, onPartnerClick = onPartnerClick, isLoading = isLoading)
            }
        }
    }
}
