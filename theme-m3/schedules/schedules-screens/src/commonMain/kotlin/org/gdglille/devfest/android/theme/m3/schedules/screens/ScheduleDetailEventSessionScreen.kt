package org.gdglille.devfest.android.theme.m3.schedules.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import org.gdglille.devfest.android.shared.resources.Resource
import org.gdglille.devfest.android.shared.resources.screen_schedule_detail_event_session
import org.gdglille.devfest.android.theme.m3.style.SpacingTokens
import org.gdglille.devfest.android.theme.m3.style.appbars.TopAppBar
import org.gdglille.devfest.android.theme.m3.style.toDp
import org.gdglille.devfest.models.ui.EventSessionItemUi
import org.gdglille.devfest.theme.m3.schedules.ui.schedule.EventSessionSection
import org.gdglille.devfest.theme.m3.schedules.ui.schedule.TalkAbstract
import org.gdglille.devfest.theme.m3.style.events.cards.AddressCard
import org.jetbrains.compose.resources.stringResource

@ExperimentalMaterial3Api
@Composable
fun ScheduleDetailEventSessionScreen(
    session: EventSessionItemUi,
    onItineraryClicked: (lat: Double, lng: Double) -> Unit,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = stringResource(Resource.string.screen_schedule_detail_event_session),
                navigationIcon = { Back(onClick = onBackClicked) },
                scrollBehavior = scrollBehavior
            )
        },
        content = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SpacingTokens.LargeSpacing.toDp()),
                contentPadding = it,
                verticalArrangement = Arrangement.spacedBy(SpacingTokens.LargeSpacing.toDp()),
                state = state
            ) {
                item {
                    Spacer(modifier = Modifier.height(SpacingTokens.LargeSpacing.toDp()))
                    EventSessionSection(session = session)
                }
                session.description?.let {
                    item {
                        TalkAbstract(abstract = it)
                    }
                }
                session.addressUi?.let {
                    item {
                        AddressCard(
                            formattedAddress = it.formattedAddress,
                            onItineraryClicked = {
                                onItineraryClicked(it.latitude, it.longitude)
                            },
                            hasGpsLocation = true
                        )
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(SpacingTokens.ExtraLargeSpacing.toDp()))
                }
            }
        }
    )
}
