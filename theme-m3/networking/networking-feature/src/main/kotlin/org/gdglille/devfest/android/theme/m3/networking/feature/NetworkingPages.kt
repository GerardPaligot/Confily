package org.gdglille.devfest.android.theme.m3.networking.feature

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import org.gdglille.devfest.android.theme.m3.style.actions.TabActionsUi
import org.gdglille.devfest.repositories.UserRepository

private const val MyProfileId = 0

@ExperimentalPagerApi
@Composable
fun NetworkingPages(
    tabs: TabActionsUi,
    userRepository: UserRepository,
    onCreateProfileClicked: () -> Unit,
    onProfileScreenOpened: () -> Unit,
    onContactListScreenOpened: () -> Unit,
    modifier: Modifier = Modifier,
    pagerState: PagerState = rememberPagerState(),
) {
    LaunchedEffect(pagerState.currentPage) {
        when (pagerState.currentPage) {
            MyProfileId -> onProfileScreenOpened()
            else -> onContactListScreenOpened()
        }
    }
    val count = tabs.actions.count()
    HorizontalPager(count = if (count == MyProfileId) 1 else count, state = pagerState) { page ->
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
