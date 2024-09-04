package com.paligot.confily.partners.panes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.paligot.confily.models.ui.PartnerItemUi
import com.paligot.confily.partners.ui.PartnerDetailSectionVertical
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.title_jobs
import com.paligot.confily.resources.title_plan_partner
import com.paligot.confily.style.components.placeholder.placeholder
import com.paligot.confily.style.events.cards.AddressCard
import com.paligot.confily.style.partners.jobs.JobItem
import org.jetbrains.compose.resources.stringResource

@Composable
fun PartnerDetailVerticalScreen(
    partnerItemUi: PartnerItemUi,
    onLinkClicked: (url: String) -> Unit,
    onItineraryClicked: (lat: Double, lng: Double) -> Unit,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    isLoading: Boolean = false,
    displayAvatar: Boolean = true
) {
    LazyColumn(
        modifier = modifier.padding(contentPadding),
        contentPadding = PaddingValues(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        state = state
    ) {
        item {
            PartnerDetailSectionVertical(
                partnerItemUi = partnerItemUi,
                isLoading = isLoading,
                displayAvatar = displayAvatar,
                onLinkClicked = onLinkClicked
            )
        }
        if (partnerItemUi.jobs.isNotEmpty()) {
            item {
                Text(
                    text = stringResource(Resource.string.title_jobs),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.semantics { heading() }
                )
            }
            items(partnerItemUi.jobs) {
                JobItem(
                    title = it.title,
                    description = "${it.companyName} - ${it.location}",
                    requirements = it.requirements,
                    propulsedBy = it.propulsed,
                    salaryMin = it.salary?.min,
                    salaryMax = it.salary?.max,
                    salaryRecurrence = it.salary?.recurrence,
                    onClick = { onLinkClicked(it.url) },
                    modifier = Modifier.fillMaxWidth().placeholder(visible = isLoading)
                )
            }
        }
        partnerItemUi.formattedAddress?.let {
            val hasGpsLocation = partnerItemUi.latitude != null && partnerItemUi.longitude != null
            item {
                Text(
                    text = stringResource(Resource.string.title_plan_partner),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.semantics { heading() }
                )
                Spacer(modifier = Modifier.height(8.dp))
                AddressCard(
                    formattedAddress = it,
                    isLoading = isLoading,
                    hasGpsLocation = hasGpsLocation,
                    onItineraryClicked = {
                        if (hasGpsLocation) {
                            onItineraryClicked(partnerItemUi.latitude!!, partnerItemUi.longitude!!)
                        }
                    }
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
