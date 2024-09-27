package com.paligot.confily.speakers.sample

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import com.paligot.confily.core.agenda.AgendaDao
import com.paligot.confily.core.events.EventDao
import com.paligot.confily.core.sample.BuildConfig
import com.paligot.confily.core.test.instrumentedModule
import com.paligot.confily.core.test.patterns.navigation.RobotNavigator
import com.paligot.confily.core.test.patterns.navigation.robotHost
import com.paligot.confily.speakers.di.speakersModule
import com.paligot.confily.speakers.sample.fakes.AgendaFake.agenda
import com.paligot.confily.speakers.sample.fakes.AgendaFake.category
import com.paligot.confily.speakers.sample.fakes.AgendaFake.format
import com.paligot.confily.speakers.sample.fakes.AgendaFake.schedule
import com.paligot.confily.speakers.sample.fakes.AgendaFake.session
import com.paligot.confily.speakers.sample.fakes.AgendaFake.speakerAu
import com.paligot.confily.speakers.sample.fakes.AgendaFake.speakerGe
import com.paligot.confily.speakers.sample.fakes.EventFake.event
import com.paligot.confily.speakers.test.robot.speakers
import com.paligot.confily.speakers.test.speakerRobotGraph
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
    private val agendaDao by inject<AgendaDao>()

    @Before
    fun setup() {
        eventDao.insertEventId(BuildConfig.DEFAULT_EVENT)
        eventDao.insertEvent(event, emptyList())
        agendaDao.saveAgenda(BuildConfig.DEFAULT_EVENT, agenda)
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
