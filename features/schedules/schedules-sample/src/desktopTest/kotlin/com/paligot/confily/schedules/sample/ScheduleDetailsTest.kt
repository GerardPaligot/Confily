package com.paligot.confily.schedules.sample

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.paligot.confily.core.events.EventDao
import com.paligot.confily.core.kvalue.ConferenceSettings
import com.paligot.confily.core.schedules.SessionDao
import com.paligot.confily.core.test.FakeLifecycleOwner
import com.paligot.confily.core.test.instrumentedModule
import com.paligot.confily.core.test.patterns.navigation.RobotNavigator
import com.paligot.confily.core.test.patterns.navigation.robotHost
import com.paligot.confily.schedules.di.scheduleModule
import com.paligot.confily.schedules.sample.fakes.AgendaFake.agenda
import com.paligot.confily.schedules.sample.fakes.AgendaFake.category
import com.paligot.confily.schedules.sample.fakes.AgendaFake.format
import com.paligot.confily.schedules.sample.fakes.AgendaFake.schedule
import com.paligot.confily.schedules.sample.fakes.AgendaFake.session
import com.paligot.confily.schedules.sample.fakes.AgendaFake.speaker
import com.paligot.confily.schedules.sample.fakes.EventFake.event
import com.paligot.confily.schedules.test.robot.schedules
import com.paligot.confily.schedules.test.scheduleRobotGraph
import org.junit.Rule
import org.junit.Test
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import java.util.Locale

class ScheduleDetailsTest {
    @get:Rule
    val rule = createComposeRule()

    @Test
    fun shouldOpenScheduleAndDisplaySessionAndSpeakerInfo() {
        Locale.setDefault(Locale("en", "US"))
        rule.setContent {
            KoinApplication(application = {
                modules(platformModule, scheduleModule, instrumentedModule)
            }) {
                val settings = koinInject<ConferenceSettings>()
                val eventDao = koinInject<EventDao>()
                val sessionDao = koinInject<SessionDao>()
                LaunchedEffect(Unit) {
                    settings.insertEventId(BuildKonfig.DEFAULT_EVENT)
                    eventDao.insertEvent(event)
                    sessionDao.insertAgenda(BuildKonfig.DEFAULT_EVENT, agenda)
                }
                val lifecycleOwner = remember { FakeLifecycleOwner() }
                CompositionLocalProvider(
                    LocalLifecycleOwner provides lifecycleOwner
                ) {
                    App(
                        isPortrait = true,
                        onScheduleStarted = {}
                    )
                }
            }
        }
        val navigator = RobotNavigator()
        robotHost(navigator) {
            scheduleRobotGraph(navigator, rule)
        }
        schedules(navigator) {
            assertSessionIsShown(show = true, session.title, listOf(speaker.displayName), schedule.room, format.time, category.name)
        } goToSecheduleDetail {
            assertSessionDetails(
                session.title,
                session.abstract,
                listOf(speaker.displayName),
                schedule.room,
                format.time,
                category.name
            )
        } backToScheduleGrid {
        }
    }
}
