package org.gdglille.devfest.android.theme.m3.speakers.test

import androidx.compose.ui.test.junit4.ComposeTestRule
import org.gdglille.devfest.android.core.test.patterns.navigation.NavRobotGraph
import org.gdglille.devfest.android.core.test.patterns.navigation.RobotNavigator
import org.gdglille.devfest.android.core.test.patterns.navigation.robot
import org.gdglille.devfest.android.theme.m3.speakers.test.robot.SpeakerDetailRobot
import org.gdglille.devfest.android.theme.m3.speakers.test.robot.SpeakersGridRobot
import org.gdglille.devfest.android.theme.m3.speakers.test.scopes.SpeakerDetailRobotScope
import org.gdglille.devfest.android.theme.m3.speakers.test.scopes.SpeakersGridRobotScope

fun NavRobotGraph.speakerRobotGraph(
    navigator: RobotNavigator,
    composeTestRule: ComposeTestRule
) {
    robot<SpeakersGridRobotScope>(SpeakersGridRobot(navigator, composeTestRule))
    robot<SpeakerDetailRobotScope>(SpeakerDetailRobot(navigator, composeTestRule))
}
