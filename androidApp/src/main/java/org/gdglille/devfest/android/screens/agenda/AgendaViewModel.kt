package org.gdglille.devfest.android.screens.agenda

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.os.SystemClock
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.gdglille.devfest.android.AlarmReceiver
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
                        if (it.talks.isNotEmpty()) {
                            _uiState.value = AgendaUiState.Success(it)
                        }
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
        val intent = AlarmReceiver.create(
            context, talkItem.id, "Talk dans 10 minutes en ${talkItem.room.lowercase(Locale.getDefault())}", talkItem.title
        )
        val pendingIntent = PendingIntent.getBroadcast(
            context, talkItem.id.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT
        )
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
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                AgendaViewModel(
                    repository = repository,
                    alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                ) as T
        }
    }
}
