package org.gdglille.devfest.android.theme.m3.speakers.test

import androidx.compose.ui.test.junit4.ComposeTestRule
import com.paligot.confily.core.test.patterns.navigation.NavRobotGraph
import com.paligot.confily.core.test.patterns.navigation.RobotNavigator
import com.paligot.confily.core.test.patterns.navigation.robot
import com.paligot.confily.speakers.test.scopes.SpeakerDetailRobotScope
import com.paligot.confily.speakers.test.scopes.SpeakersGridRobotScope
import org.gdglille.devfest.android.theme.m3.speakers.test.robot.SpeakerDetailRobot
import org.gdglille.devfest.android.theme.m3.speakers.test.robot.SpeakersGridRobot

fun NavRobotGraph.speakerRobotGraph(
    navigator: RobotNavigator,
    composeTestRule: ComposeTestRule
) {
    robot<SpeakersGridRobotScope>(SpeakersGridRobot(navigator, composeTestRule))
    robot<SpeakerDetailRobotScope>(SpeakerDetailRobot(navigator, composeTestRule))
}
