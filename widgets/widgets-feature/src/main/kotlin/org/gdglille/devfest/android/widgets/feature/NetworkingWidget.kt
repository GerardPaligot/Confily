package org.gdglille.devfest.android.widgets.feature

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.Action
import androidx.glance.background
import org.gdglille.devfest.android.widgets.screens.NetworkingScreen
import org.gdglille.devfest.android.widgets.ui.Loading
import org.gdglille.devfest.repositories.UserRepository

@Composable
fun NetworkingWidget(
    userRepository: UserRepository,
    @DrawableRes iconId: Int,
    onNewProfile: Action,
    onMyProfile: Action,
    modifier: GlanceModifier = GlanceModifier
) {
    val viewModel = remember { NetworkingViewModel(userRepository) }
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is NetworkingUiState.Loading -> {
            Loading(modifier = modifier.background(GlanceTheme.colors.widgetBackground))
        }

        is NetworkingUiState.Success -> {
            NetworkingScreen(
                userInfo = uiState.user,
                iconId = iconId,
                onNewProfile = onNewProfile,
                onMyProfile = onMyProfile,
                modifier = modifier
            )
        }
    }
}
