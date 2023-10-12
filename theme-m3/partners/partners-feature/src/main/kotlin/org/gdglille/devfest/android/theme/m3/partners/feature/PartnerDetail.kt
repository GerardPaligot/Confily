package org.gdglille.devfest.android.theme.m3.partners.feature

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.theme.m3.style.appbars.TopAppBar
import org.gdglille.devfest.android.theme.m3.style.cards.AddressCard
import org.gdglille.devfest.android.theme.m3.partners.ui.jobs.JobItem
import org.gdglille.devfest.android.theme.m3.partners.ui.partners.PartnerDetailSection
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.android.theme.m3.style.R
import org.gdglille.devfest.models.ui.PartnerItemUi

@ExperimentalMaterial3Api
@Composable
fun PartnerDetail(
    partnerItemUi: PartnerItemUi,
    onLinkClicked: (url: String) -> Unit,
    onItineraryClicked: (lat: Double, lng: Double) -> Unit,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = stringResource(id = R.string.screen_partners_detail),
                navigationIcon = { Back(onClick = onBackClicked) }
            )
        },
        content = {
            LazyColumn(
                contentPadding = it,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    PartnerDetailSection(
                        partnerItemUi = partnerItemUi,
                        isLoading = isLoading,
                        onLinkClicked = onLinkClicked
                    )
                }
                if (partnerItemUi.jobs.isNotEmpty()) {
                    item {
                        Text(
                            text = stringResource(R.string.title_jobs),
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .semantics { heading() }
                        )
                    }
                    items(partnerItemUi.jobs) {
                        JobItem(
                            jobUi = it,
                            isLoading = isLoading,
                            onClick = onLinkClicked,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        )
                    }
                }
                partnerItemUi.formattedAddress?.let {
                    val hasGpsLocation = partnerItemUi.latitude != null && partnerItemUi.longitude != null
                    item {
                        Text(
                            text = stringResource(R.string.title_plan_partner),
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(horizontal = 16.dp).semantics { heading() }
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
                            },
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun PartnerDetailPreview() {
    Conferences4HallTheme {
        PartnerDetail(
            partnerItemUi = PartnerItemUi.fake,
            onLinkClicked = {},
            onItineraryClicked = { _, _ -> },
            onBackClicked = {}
        )
    }
}
