package org.gdglille.devfest.android.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.components.events.EventSection
import org.gdglille.devfest.android.components.events.PartnerDivider
import org.gdglille.devfest.android.components.events.PartnerRow
import org.gdglille.devfest.android.ui.R
import org.gdglille.devfest.models.EventUi

@Composable
fun Event(
    logo: Int,
    event: EventUi,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    onFaqClick: (url: String) -> Unit,
    onCoCClick: (url: String) -> Unit,
    onTwitterClick: (url: String?) -> Unit,
    onLinkedInClick: (url: String?) -> Unit,
    onPartnerClick: (siteUrl: String?) -> Unit
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 8.dp),
        contentPadding = PaddingValues(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            EventSection(
                logo = logo,
                eventInfo = event.eventInfo,
                isLoading = isLoading,
                onFaqClick = onFaqClick,
                onCoCClick = onCoCClick,
                onTwitterClick = onTwitterClick,
                onLinkedInClick = onLinkedInClick
            )
        }
        item { PartnerDivider(title = stringResource(id = R.string.title_gold)) }
        items(event.partners.golds) {
            PartnerRow(partners = it, onPartnerClick = onPartnerClick, isLoading = isLoading)
        }
        item { PartnerDivider(title = stringResource(id = R.string.title_silver)) }
        items(event.partners.silvers) {
            PartnerRow(partners = it, onPartnerClick = onPartnerClick, isLoading = isLoading)
        }
        item { PartnerDivider(title = stringResource(id = R.string.title_bronze)) }
        items(event.partners.bronzes) {
            PartnerRow(partners = it, onPartnerClick = onPartnerClick, isLoading = isLoading)
        }
    }
}
