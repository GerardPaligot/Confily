package com.paligot.confily.speakers.sample

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
import org.junit.Rule
import org.junit.Test
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import java.util.Locale

class SpeakerInfoItemDetailsTest {
    @get:Rule
    val rule = createComposeRule()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun shouldOpenSpeakerInfo() {
        Locale.setDefault(Locale("en", "US"))
        rule.setContent {
            KoinApplication(application = {
                modules(platformModule, speakersModule, instrumentedModule)
            }) {
                val settings = koinInject<ConferenceSettings>()
                val eventDao = koinInject<EventDao>()
                val sessionDao = koinInject<SessionDao>()
                LaunchedEffect(Unit) {
                    settings.insertEventId(BuildKonfig.DEFAULT_EVENT)
                    eventDao.insertEvent(event, emptyList(), emptyMap())
                    sessionDao.insertAgenda(BuildKonfig.DEFAULT_EVENT, agenda)
                }
                val lifecycleOwner = remember { FakeLifecycleOwner() }
                CompositionLocalProvider(
                    LocalLifecycleOwner provides lifecycleOwner
                ) {
                    App(
                        isLandscape = true,
                        launchUrl = {},
                        adaptiveInfo = WindowSizeClass.calculateFromSize(
                            size = DpSize(240.dp, 160.dp)
                        )
                    )
                }
            }
        }
        val navigator = RobotNavigator()
        robotHost(navigator) {
            speakerRobotGraph(navigator, rule)
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
