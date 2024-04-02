package org.gdglille.devfest.android.theme.m3.partners.screens

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.shared.resources.Resource
import org.gdglille.devfest.android.shared.resources.title_jobs
import org.gdglille.devfest.android.shared.resources.title_plan_partner
import org.gdglille.devfest.android.theme.m3.partners.ui.partners.PartnerDetailSectionVertical
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.android.theme.m3.style.events.cards.AddressCard
import org.gdglille.devfest.android.theme.m3.style.partners.jobs.JobItem
import org.gdglille.devfest.android.theme.m3.style.placeholder.placeholder
import org.gdglille.devfest.models.ui.PartnerItemUi
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalResourceApi::class)
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

@Preview
@Composable
private fun PartnerDetailVerticalScreenPreview() {
    Conferences4HallTheme {
        Scaffold {
            PartnerDetailVerticalScreen(
                partnerItemUi = PartnerItemUi.fake,
                onLinkClicked = {},
                onItineraryClicked = { _, _ -> },
                contentPadding = it
            )
        }
    }
}
