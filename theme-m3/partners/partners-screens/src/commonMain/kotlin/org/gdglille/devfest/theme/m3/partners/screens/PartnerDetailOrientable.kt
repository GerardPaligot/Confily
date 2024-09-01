package org.gdglille.devfest.theme.m3.partners.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import org.gdglille.devfest.android.shared.resources.Resource
import org.gdglille.devfest.android.shared.resources.screen_partners_detail
import org.gdglille.devfest.android.theme.m3.style.appbars.AppBarIcons
import org.gdglille.devfest.android.theme.m3.style.appbars.TopAppBar
import org.gdglille.devfest.models.ui.PartnerItemUi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartnerDetailOrientable(
    partnerItemUi: PartnerItemUi,
    onLinkClicked: (url: String) -> Unit,
    onItineraryClicked: (lat: Double, lng: Double) -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (AppBarIcons.() -> Unit)? = null,
    isLandscape: Boolean = false,
    isLoading: Boolean = false
) {
    val state = rememberLazyListState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = stringResource(Resource.string.screen_partners_detail),
                navigationIcon = navigationIcon,
                scrollBehavior = scrollBehavior
            )
        },
        content = {
            if (isLandscape) {
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.padding(it)
                ) {
                    PartnerImageScreen(
                        url = partnerItemUi.logoUrl,
                        isLoading = isLoading,
                        modifier = Modifier.weight(1f)
                    )
                    PartnerDetailVerticalScreen(
                        partnerItemUi = partnerItemUi,
                        onLinkClicked = onLinkClicked,
                        onItineraryClicked = onItineraryClicked,
                        modifier = Modifier.weight(1f),
                        state = state,
                        isLoading = isLoading,
                        displayAvatar = false
                    )
                }
            } else {
                PartnerDetailVerticalScreen(
                    partnerItemUi = partnerItemUi,
                    onLinkClicked = onLinkClicked,
                    onItineraryClicked = onItineraryClicked,
                    contentPadding = it,
                    state = state,
                    isLoading = isLoading
                )
            }
        }
    )
}
