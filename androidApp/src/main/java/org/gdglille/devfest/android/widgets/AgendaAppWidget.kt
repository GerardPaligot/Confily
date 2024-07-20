package org.gdglille.devfest.android.widgets

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.provideContent
import androidx.glance.currentState
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.gdglille.devfest.android.R
import org.gdglille.devfest.android.widgets.feature.SessionsWidget
import org.gdglille.devfest.android.widgets.style.Conferences4HallGlanceTheme
import org.gdglille.devfest.repositories.AgendaRepository
import org.gdglille.devfest.repositories.EventRepository
import org.gdglille.devfest.theme.m3.navigation.Screen
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AgendaAppWidget : GlanceAppWidget(), KoinComponent {
    private val agendaRepository: AgendaRepository by inject()
    private val eventRepository: EventRepository by inject()

    override val sizeMode = SizeMode.Exact

    override val stateDefinition: GlanceStateDefinition<*>
        get() = PreferencesGlanceStateDefinition

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val date = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).toString()
        provideContent {
            Conferences4HallGlanceTheme {
                val prefs = currentState<Preferences>()
                SessionsWidget(
                    eventRepository = eventRepository,
                    agendaRepository = agendaRepository,
                    date = prefs.lastUpdate ?: date,
                    iconId = R.drawable.ic_launcher_foreground,
                    onUpdate = {
                        prefs.toMutablePreferences().apply {
                            this.lastUpdate = Clock.System.now()
                                .toLocalDateTime(TimeZone.currentSystemDefault())
                                .toString()
                        }
                        update(context, id)
                    },
                    onItemClick = {
                        actionStartActivity(
                            intent = Intent(
                                Intent.ACTION_VIEW,
                                "c4h://event/${Screen.Schedule.route(it)}".toUri()
                            )
                        )
                    }
                )
            }
        }
    }
}

var MutablePreferences.lastUpdate: String?
    set(value) {
        this[stringPreferencesKey("last_update")] = value ?: ""
    }
    get() = this[stringPreferencesKey("last_update")]

val Preferences.lastUpdate: String?
    get() = this[stringPreferencesKey("last_update")]
