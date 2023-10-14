package org.gdglille.devfest.android.theme.m3.networking.feature

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import org.gdglille.devfest.android.theme.m3.navigation.TabActions
import org.gdglille.devfest.android.theme.m3.style.actions.TabActionsUi
import org.gdglille.devfest.repositories.UserRepository

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NetworkingPages(
    tabs: TabActionsUi,
    userRepository: UserRepository,
    pagerState: PagerState,
    onCreateProfileClicked: () -> Unit,
    onInnerScreenOpened: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(pagerState.currentPage) {
        onInnerScreenOpened(tabs.actions[pagerState.currentPage].route)
    }
    HorizontalPager(state = pagerState) { page ->
        when (tabs.actions[page].route) {
            TabActions.myProfile.route -> MyProfileVM(
                userRepository = userRepository,
                modifier = modifier.fillMaxSize(),
                onEditInformation = onCreateProfileClicked
            )

            TabActions.contacts.route -> ContactsVM(
                userRepository = userRepository,
                modifier = modifier.fillMaxSize()
            )

            else -> TODO("Screen not implemented")
        }
    }
}
