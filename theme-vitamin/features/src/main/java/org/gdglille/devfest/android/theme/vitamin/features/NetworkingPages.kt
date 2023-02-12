package org.gdglille.devfest.android.theme.vitamin.features

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import org.gdglille.devfest.android.theme.vitamin.features.viewmodels.HomeViewModel
import org.gdglille.devfest.android.theme.vitamin.ui.Screen
import org.gdglille.devfest.android.ui.resources.models.TabActionsUi
import org.gdglille.devfest.repositories.UserRepository

private const val MyProfileId = 0

@ExperimentalPagerApi
@Composable
fun NetworkingPages(
    tabs: TabActionsUi,
    userRepository: UserRepository,
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier,
    pagerState: PagerState = rememberPagerState(),
    onCreateProfileClicked: () -> Unit
) {
    LaunchedEffect(pagerState.currentPage) {
        when (pagerState.currentPage) {
            MyProfileId -> viewModel.updateFabUi(Screen.MyProfile.route)
            else -> viewModel.updateFabUi(Screen.Contacts.route)
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
