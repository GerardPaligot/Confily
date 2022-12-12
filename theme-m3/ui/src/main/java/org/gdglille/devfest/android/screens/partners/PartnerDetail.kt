package org.gdglille.devfest.android.screens.partners

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Brands
import compose.icons.fontawesomeicons.brands.Linkedin
import compose.icons.fontawesomeicons.brands.Twitter
import org.gdglille.devfest.android.components.appbars.TopAppBar
import org.gdglille.devfest.android.components.cards.AddressCard
import org.gdglille.devfest.android.components.cards.SocialCard
import org.gdglille.devfest.android.components.partners.PartnerDetailSection
import org.gdglille.devfest.android.theme.Conferences4HallTheme
import org.gdglille.devfest.android.theme.m3.ui.R
import org.gdglille.devfest.models.PartnerItemUi

@ExperimentalMaterial3Api
@Composable
fun PartnerDetail(
    partnerItemUi: PartnerItemUi,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    onLinkClicked: (url: String) -> Unit,
    onItineraryClicked: (lat: Double, lng: Double) -> Unit,
    onBackClicked: () -> Unit
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
                        modifier = modifier,
                        isLoading = isLoading,
                        onLinkClicked = onLinkClicked
                    )
                }
                partnerItemUi.formattedAddress?.let {
                    val hasGpsLocation = partnerItemUi.latitude != null && partnerItemUi.longitude != null
                    item {
                        Text(
                            text = stringResource(R.string.title_plan_partner),
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(horizontal = 16.dp)
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
                if (partnerItemUi.twitterMessage != null || partnerItemUi.linkedinMessage != null) {
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = stringResource(R.string.title_communication),
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            partnerItemUi.twitterMessage?.let { twitterMessage ->
                                SocialCard(
                                    title = stringResource(R.string.title_communication_twitter),
                                    text = twitterMessage,
                                    imageVector = FontAwesomeIcons.Brands.Twitter,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                            }
                            partnerItemUi.linkedinMessage?.let { linkedinMessage ->
                                SocialCard(
                                    title = stringResource(R.string.title_communication_linkedin),
                                    text = linkedinMessage,
                                    imageVector = FontAwesomeIcons.Brands.Linkedin,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                            }
                        }
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
