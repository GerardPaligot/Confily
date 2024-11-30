package com.paligot.confily.widgets.presentation

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.Action
import androidx.glance.background
import com.paligot.confily.core.networking.UserRepository
import com.paligot.confily.widgets.panes.NetworkingScreen
import com.paligot.confily.widgets.ui.Loading

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
