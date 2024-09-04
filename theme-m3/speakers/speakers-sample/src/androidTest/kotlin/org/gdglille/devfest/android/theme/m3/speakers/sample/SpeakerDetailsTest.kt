package org.gdglille.devfest.android.theme.m3.speakers.sample

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import com.paligot.confily.core.database.EventDao
import com.paligot.confily.core.database.ScheduleDao
import com.paligot.confily.core.sample.BuildConfig
import com.paligot.confily.core.test.instrumentedModule
import com.paligot.confily.core.test.patterns.navigation.RobotNavigator
import com.paligot.confily.core.test.patterns.navigation.robotHost
import com.paligot.confily.speakers.di.speakersModule
import com.paligot.confily.speakers.test.robot.speakers
import com.paligot.confily.speakers.test.speakerRobotGraph
import org.gdglille.devfest.android.theme.m3.speakers.sample.fakes.AgendaFake.agenda
import org.gdglille.devfest.android.theme.m3.speakers.sample.fakes.AgendaFake.category
import org.gdglille.devfest.android.theme.m3.speakers.sample.fakes.AgendaFake.format
import org.gdglille.devfest.android.theme.m3.speakers.sample.fakes.AgendaFake.schedule
import org.gdglille.devfest.android.theme.m3.speakers.sample.fakes.AgendaFake.session
import org.gdglille.devfest.android.theme.m3.speakers.sample.fakes.AgendaFake.speakerAu
import org.gdglille.devfest.android.theme.m3.speakers.sample.fakes.AgendaFake.speakerGe
import org.gdglille.devfest.android.theme.m3.speakers.sample.fakes.EventFake.event
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject

class SpeakerDetailsTest : KoinTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        androidContext(InstrumentationRegistry.getInstrumentation().targetContext.applicationContext)
        modules(listOf(speakersModule, instrumentedModule))
    }

    private val eventDao by inject<EventDao>()
    private val scheduleDao by inject<ScheduleDao>()

    @Before
    fun setup() {
        eventDao.insertEventId(BuildConfig.DEFAULT_EVENT)
        eventDao.insertEvent(event, emptyList())
        scheduleDao.saveAgenda(BuildConfig.DEFAULT_EVENT, agenda)
    }

    @Test
    fun shouldOpenSpeakerInfo() {
        ActivityScenario.launch(MainActivity::class.java).use {
            val navigator = RobotNavigator()
            robotHost(navigator) {
                speakerRobotGraph(navigator, composeTestRule)
            }
            speakers(navigator) {
                assertSpeakerScreenIsDisplayed()
                assertSpeakerItemsIsDisplayed(listOf(speakerGe.displayName, speakerAu.displayName))
            }.clickSpeakerItem(speakerGe.displayName) {
                assertSpeakerInfoAreDisplayed(
                    speakerGe.displayName,
                    speakerGe.company,
                    speakerGe.bio
                )
                assertSpeakerTalkIsDisplayed(
                    session.title,
                    listOf(speakerGe.displayName),
                    schedule.room,
                    format.time,
                    category.name
                )
            }.backToSpeakersScreen {
                assertSpeakerScreenIsDisplayed()
            }
        }
    }
}
