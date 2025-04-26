package com.paligot.confily.partners.panes

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
import com.paligot.confily.partners.ui.models.PartnerUi
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.screen_partners_detail
import com.paligot.confily.style.theme.appbars.AppBarIcons
import com.paligot.confily.style.theme.appbars.TopAppBar
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartnerDetailPane(
    partnerUi: PartnerUi,
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
                    PartnerImageContent(
                        url = partnerUi.logoUrl,
                        isLoading = isLoading,
                        modifier = Modifier.weight(1f)
                    )
                    PartnerDetailContent(
                        partnerUi = partnerUi,
                        onLinkClicked = onLinkClicked,
                        onItineraryClicked = onItineraryClicked,
                        modifier = Modifier.weight(1f),
                        state = state,
                        isLoading = isLoading,
                        displayAvatar = false
                    )
                }
            } else {
                PartnerDetailContent(
                    partnerUi = partnerUi,
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
