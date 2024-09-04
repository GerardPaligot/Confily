package org.gdglille.devfest.baselineprofile.scenarios

import androidx.test.uiautomator.By
import org.gdglille.devfest.android.theme.m3.partners.semantics.PartnersSemantics
import com.paligot.confily.schedules.semantics.SchedulesSemantics
import com.paligot.confily.speakers.semantics.SpeakersSemantics

object DevfestScenario {
    val back = By.desc("Back")
    object Event {
        val pastEvents = By.text("Past events")
        val name = By.text("Devfest Lille 2023")
    }
    object Agenda {
        val tabName = By.desc("Agenda")
        val filterAction = By.desc("Open filter screen")
        val first = By.desc("Keynote d'ouverture  in Grand Théâtre room during 50 minutes in category Discovery for level Beginner")
        val list = By.res(SchedulesSemantics.list)
    }
    object Speakers {
        val tabName = By.desc("Speakers")
        val first = By.text("Thanh Lan DOUBLIER")
        val list = By.res(SpeakersSemantics.list)
    }
    object Networking {
        val tabName = By.desc("Networking")
    }
    object Partners {
        val tabName = By.desc("Partners")
        val first = By.desc("SFEIR")
        val detailTitle = By.text("Partner detail")
        val list = By.res(PartnersSemantics.list)
    }
    object Info {
        val tabName = By.desc("Info")
        val overflowAction = By.desc("Display overflow menu")
        val changeEventAction = By.text("Change event")
    }
}
