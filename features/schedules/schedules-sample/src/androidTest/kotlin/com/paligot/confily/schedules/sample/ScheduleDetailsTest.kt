package com.paligot.confily.schedules.sample

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import com.paligot.confily.core.agenda.AgendaDao
import com.paligot.confily.core.db.ConferenceSettings
import com.paligot.confily.core.sample.BuildConfig
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
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject

class ScheduleDetailsTest : KoinTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        androidContext(InstrumentationRegistry.getInstrumentation().targetContext.applicationContext)
        modules(listOf(scheduleModule, instrumentedModule))
    }

    private val settings by inject<ConferenceSettings>()
    private val agendaDao by inject<AgendaDao>()

    @Before
    fun setup() {
        settings.insertEventId(BuildConfig.DEFAULT_EVENT)
        agendaDao.insertEvent(event, emptyList())
        agendaDao.saveAgenda(BuildConfig.DEFAULT_EVENT, agenda)
    }

    @Test
    fun shouldOpenScheduleAndDisplaySessionAndSpeakerInfo() {
        ActivityScenario.launch(MainActivity::class.java).use {
            val navigator = RobotNavigator()
            robotHost(navigator) {
                scheduleRobotGraph(navigator, composeTestRule)
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
}
