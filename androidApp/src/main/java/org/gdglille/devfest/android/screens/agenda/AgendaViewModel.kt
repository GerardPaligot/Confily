package org.gdglille.devfest.android.screens.agenda

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.os.SystemClock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.russhwolf.settings.ExperimentalSettingsApi
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import org.gdglille.devfest.android.AlarmReceiver
import org.gdglille.devfest.android.R
import org.gdglille.devfest.models.AgendaUi
import org.gdglille.devfest.models.TalkItemUi
import org.gdglille.devfest.repositories.AgendaRepository
import java.net.UnknownHostException
import java.util.*

sealed class AgendaUiState {
    data class Loading(val agenda: AgendaUi) : AgendaUiState()
    data class Success(val agenda: AgendaUi) : AgendaUiState()
    data class Failure(val throwable: Throwable) : AgendaUiState()
}

@FlowPreview
@ExperimentalSettingsApi
@ExperimentalCoroutinesApi
@ExperimentalPermissionsApi
@ExperimentalMaterial3Api
class AgendaViewModel(
    private val repository: AgendaRepository,
    private val alarmManager: AlarmManager
) : ViewModel() {
    private val _uiState = MutableStateFlow<AgendaUiState>(AgendaUiState.Loading(AgendaUi.fake))
    val uiState: StateFlow<AgendaUiState> = _uiState

    init {
        viewModelScope.launch {
            arrayListOf(async {
                try {
                    repository.fetchAndStoreAgenda()
                } catch (ignored: UnknownHostException) {
                } catch (error: Throwable) {
                    Firebase.crashlytics.recordException(error)
                }
            }, async {
                try {
                    repository.agenda().collect {
                        _uiState.value = AgendaUiState.Success(it)
                    }
                } catch (error: Throwable) {
                    Firebase.crashlytics.recordException(error)
                    _uiState.value = AgendaUiState.Failure(error)
                }
            }).awaitAll()
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun markAsFavorite(context: Context, talkItem: TalkItemUi) = viewModelScope.launch {
        val isFavorite = !talkItem.isFavorite
        repository.markAsRead(talkItem.id, isFavorite)
        val title = context.getString(R.string.title_notif_reminder_talk, talkItem.room.lowercase(Locale.getDefault()))
        val intent = AlarmReceiver.create(context, talkItem.id, title, talkItem.title)
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        val pendingIntent = PendingIntent.getBroadcast(context, talkItem.id.hashCode(), intent, flags)
        if (isFavorite) {
            val time = talkItem.startTime
                .toLocalDateTime()
                .toInstant(TimeZone.currentSystemDefault())
                .minus(10, DateTimeUnit.MINUTE)
                .toEpochMilliseconds()
            alarmManager.set(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + (time - Clock.System.now().toEpochMilliseconds()),
                pendingIntent
            )
        } else {
            alarmManager.cancel(pendingIntent)
        }
    }

    object Factory {
        fun create(context: Context, repository: AgendaRepository) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                AgendaViewModel(
                    repository = repository,
                    alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                ) as T
        }
    }
}
