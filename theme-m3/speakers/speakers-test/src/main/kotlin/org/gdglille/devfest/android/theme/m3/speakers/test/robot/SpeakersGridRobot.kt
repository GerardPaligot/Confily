package org.gdglille.devfest.android.theme.m3.speakers.test.robot

import androidx.compose.ui.test.junit4.ComposeTestRule
import org.gdglille.devfest.android.core.test.patterns.navigation.RobotNavigator
import org.gdglille.devfest.android.core.test.patterns.navigation.navigateTo
import org.gdglille.devfest.android.theme.m3.speakers.test.pom.SpeakersGridPOM
import org.gdglille.devfest.android.theme.m3.speakers.test.scopes.SpeakerDetailRobotScope
import org.gdglille.devfest.android.theme.m3.speakers.test.scopes.SpeakersGridRobotScope

fun speakers(
    navigator: RobotNavigator,
    block: SpeakersGridRobotScope.() -> Unit
) = navigator.navigateTo<SpeakersGridRobotScope>().apply(block)

class SpeakersGridRobot(
    private val robotNavigator: RobotNavigator,
    composeTestRule: ComposeTestRule
) : SpeakersGridRobotScope {
    private val speakersGridPOM = SpeakersGridPOM(composeTestRule)

    override fun clickSpeakerItem(
        speakerName: String,
        block: SpeakerDetailRobotScope.() -> Unit
    ): SpeakerDetailRobotScope {
        speakersGridPOM.clickSpeakerItem(speakerName)
        return robotNavigator.navigateTo<SpeakerDetailRobotScope>().apply(block)
    }

    override fun assertSpeakerScreenIsDisplayed() {
        speakersGridPOM.assertSpeakerScreenIsDisplayed()
    }

    override fun assertSpeakerItemsIsDisplayed(speakers: List<String>) {
        speakersGridPOM.apply {
            assertNbSpeakerItemDisplayed(speakers.size)
            speakers.forEach { assertSpeakerItemIsDisplayed(it) }
        }
    }
}
