package org.gdglille.devfest.android.widgets.feature

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.Action
import androidx.glance.background
import com.paligot.confily.core.repositories.AgendaRepository
import com.paligot.confily.core.repositories.EventRepository
import com.paligot.confily.widgets.ui.Loading
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.gdglille.devfest.android.widgets.screens.SessionsScreen

@Composable
fun SessionsWidget(
    eventRepository: EventRepository,
    agendaRepository: AgendaRepository,
    date: String,
    @DrawableRes iconId: Int,
    onUpdate: suspend CoroutineScope.() -> Unit,
    onItemClick: (String) -> Action,
    modifier: GlanceModifier = GlanceModifier
) {
    val scope = rememberCoroutineScope()
    val viewModel = remember(date) { SessionsViewModel(agendaRepository, eventRepository, date) }
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is SessionsUiState.Loading -> {
            Loading(modifier = modifier.background(GlanceTheme.colors.widgetBackground))
        }

        is SessionsUiState.Success -> {
            LaunchedEffect(uiState.sessions.isNotEmpty()) {
                onUpdate()
            }
            SessionsScreen(
                iconId = iconId,
                onClick = { scope.launch { onUpdate() } },
                onItemClick = onItemClick,
                eventInfoUi = uiState.event,
                talks = uiState.sessions,
                modifier = modifier
            )
        }
    }
}
