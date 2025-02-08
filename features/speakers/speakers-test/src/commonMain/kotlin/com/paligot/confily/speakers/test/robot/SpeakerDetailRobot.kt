package com.paligot.confily.speakers.test.robot

import androidx.compose.ui.test.junit4.ComposeTestRule
import com.paligot.confily.core.test.patterns.navigation.RobotNavigator
import com.paligot.confily.core.test.patterns.navigation.navigateTo
import com.paligot.confily.speakers.test.pom.SpeakerDetailPage
import com.paligot.confily.speakers.test.scopes.SpeakerDetailRobotScope
import com.paligot.confily.speakers.test.scopes.SpeakersGridRobotScope

class SpeakerDetailRobot(
    private val navigator: RobotNavigator,
    composeTestRule: ComposeTestRule
) : SpeakerDetailRobotScope {
    private val speakerDetailPage = SpeakerDetailPage(composeTestRule)

    override fun assertSpeakerInfoAreDisplayed(name: String, company: String?, bio: String) {
        speakerDetailPage.apply {
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
        speakerDetailPage.assertSpeakerTalkIsDisplayed(title, speakers, room, time, category)
    }

    override fun backToSpeakersScreen(block: SpeakersGridRobotScope.() -> Unit): SpeakersGridRobotScope {
        speakerDetailPage.backToSpeakersScreen()
        return navigator.navigateTo<SpeakersGridRobotScope>().apply(block)
    }
}
