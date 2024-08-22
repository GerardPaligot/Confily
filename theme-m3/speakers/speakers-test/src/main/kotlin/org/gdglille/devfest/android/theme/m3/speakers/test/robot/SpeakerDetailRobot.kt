package org.gdglille.devfest.android.theme.m3.speakers.test.robot

import androidx.compose.ui.test.junit4.ComposeTestRule
import org.gdglille.devfest.android.core.test.patterns.navigation.RobotNavigator
import org.gdglille.devfest.android.core.test.patterns.navigation.navigateTo
import org.gdglille.devfest.android.theme.m3.speakers.test.pom.SpeakerDetailPOM
import org.gdglille.devfest.android.theme.m3.speakers.test.scopes.SpeakerDetailRobotScope
import org.gdglille.devfest.android.theme.m3.speakers.test.scopes.SpeakersGridRobotScope

class SpeakerDetailRobot(
    private val navigator: RobotNavigator,
    composeTestRule: ComposeTestRule
) : SpeakerDetailRobotScope {
    private val speakerDetailPom = SpeakerDetailPOM(composeTestRule)

    override fun assertSpeakerInfoAreDisplayed(name: String, company: String?, bio: String) {
        speakerDetailPom.apply {
            assertSpeakerNameIsDisplayed(name)
            if (company != null) {
                assertSpeakerCompanyIsDisplayed(company)
            }
            assertSpeakerBioIsDisplayed(bio)
        }
    }

    override fun assertSpeakerTalkIsDisplayed(
        title: String,
        speakers: List<String>,
        room: String,
        time: Int,
        category: String
    ) {
        speakerDetailPom.assertSpeakerTalkIsDisplayed(title, speakers, room, time, category)
    }

    override fun backToSpeakersScreen(block: SpeakersGridRobotScope.() -> Unit): SpeakersGridRobotScope {
        speakerDetailPom.backToSpeakersScreen()
        return navigator.navigateTo<SpeakersGridRobotScope>().apply(block)
    }
}
