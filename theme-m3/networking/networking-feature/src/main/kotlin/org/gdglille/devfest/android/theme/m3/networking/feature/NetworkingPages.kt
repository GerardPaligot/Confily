package org.gdglille.devfest.android.theme.m3.networking.feature

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import org.gdglille.devfest.repositories.UserRepository

private const val MyProfileId = 0

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NetworkingPages(
    userRepository: UserRepository,
    pagerState: PagerState,
    onCreateProfileClicked: () -> Unit,
    onProfileScreenOpened: () -> Unit,
    onContactListScreenOpened: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(pagerState.currentPage) {
        when (pagerState.currentPage) {
            MyProfileId -> onProfileScreenOpened()
            else -> onContactListScreenOpened()
        }
    }
    HorizontalPager(state = pagerState) { page ->
        when (page) {
            MyProfileId -> MyProfileVM(
                userRepository = userRepository,
                modifier = modifier.fillMaxSize(),
                onEditInformation = onCreateProfileClicked
            )

            else -> ContactsVM(
                userRepository = userRepository,
                modifier = modifier.fillMaxSize()
            )
        }
    }
}
