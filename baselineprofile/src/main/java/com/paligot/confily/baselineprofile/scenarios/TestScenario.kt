package com.paligot.confily.baselineprofile.scenarios

import androidx.test.uiautomator.By
import com.paligot.confily.partners.semantics.PartnersSemantics
import com.paligot.confily.schedules.semantics.SchedulesSemantics
import com.paligot.confily.speakers.semantics.SpeakersSemantics

object TestScenario {
    val back = By.desc("Back")
    object Event {
        val pastEvents = By.text("Past events")
        val name = By.text("Droidcon London")
    }
    object Agenda {
        val tabName = By.desc("Agenda")
        val filterAction = By.desc("Open filter screen")
        val first = By.desc("Project Sparkles: how Compose is changing Android Studio with Sebastiano Poggi, Chris Sinco as speaker in Hangouts room during 45 minutes in category Jetpack Compose for level Beginner")
        val list = By.res(SchedulesSemantics.list)
    }
    object Speakers {
        val tabName = By.desc("Speakers")
        val first = By.text("Alex Vanyo")
        val list = By.res(SpeakersSemantics.list)
    }
    object Networking {
        val tabName = By.desc("Networking")
    }
    object Partners {
        val tabName = By.desc("Partners")
        val first = By.desc("appdome")
        val detailTitle = By.text("Partner detail")
        val list = By.res(PartnersSemantics.list)
    }
    object Info {
        val tabName = By.desc("Info")
        val overflowAction = By.desc("Display overflow menu")
        val changeEventAction = By.text("Change event")
    }
}
