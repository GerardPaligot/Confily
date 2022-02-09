package com.paligot.conferences.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.paligot.conferences.models.EventUi
import com.paligot.conferences.ui.components.events.EventSection
import com.paligot.conferences.ui.components.events.PartnerDivider
import com.paligot.conferences.ui.components.events.PartnerRow

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
        item { PartnerDivider(title = "Gold") }
        items(event.partners.golds.chunked(3)) {
            PartnerRow(partners = it, onPartnerClick = onPartnerClick, isLoading = isLoading)
        }
        item { PartnerDivider(title = "Silver") }
        items(event.partners.silvers.chunked(3)) {
            PartnerRow(partners = it, onPartnerClick = onPartnerClick, isLoading = isLoading)
        }
        item { PartnerDivider(title = "Bronze") }
        items(event.partners.bronzes.chunked(3)) {
            PartnerRow(partners = it, onPartnerClick = onPartnerClick, isLoading = isLoading)
        }
    }
}
