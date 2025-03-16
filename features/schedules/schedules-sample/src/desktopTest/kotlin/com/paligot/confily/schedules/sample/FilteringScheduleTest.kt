package com.paligot.confily.schedules.sample

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
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

class FilteringScheduleTest {
    @get:Rule
    val rule = createComposeRule()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun shouldDisplayOnlyFavoriteSessionItem() {
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
                    eventDao.insertEvent(
                        event = event,
                        qAndA = emptyList(),
                        teamMembers = emptyMap()
                    )
                    sessionDao.insertAgenda(BuildKonfig.DEFAULT_EVENT, agenda)
                }
                val lifecycleOwner = remember { FakeLifecycleOwner() }
                CompositionLocalProvider(
                    LocalLifecycleOwner provides lifecycleOwner
                ) {
                    App(
                        isPortrait = true,
                        adaptiveInfo = WindowSizeClass.calculateFromSize(
                            size = DpSize(width = 240.dp, height = 600.dp)
                        ),
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
        } goToFilterScreen {
            assertFilterScreenIsOpened()
            selectOnlyFavorites()
        } backToScheduleGrid {
            assertSessionIsShown(show = false, session.title, listOf(speaker.displayName), schedule.room, format.time, category.name)
        } goToFilterScreen {
            assertFilterScreenIsOpened()
            selectOnlyFavorites()
        } backToScheduleGrid {
            assertSessionIsShown(show = true, session.title, listOf(speaker.displayName), schedule.room, format.time, category.name)
            addSessionToFavorites(session.title, listOf(speaker.displayName), schedule.room, format.time, category.name)
        } goToFilterScreen {
            assertFilterScreenIsOpened()
            selectOnlyFavorites()
        } backToScheduleGrid {
            assertSessionIsShown(show = true, session.title, listOf(speaker.displayName), schedule.room, format.time, category.name)
        }
    }
}
