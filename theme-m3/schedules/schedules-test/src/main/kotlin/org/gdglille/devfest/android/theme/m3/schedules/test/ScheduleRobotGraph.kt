package org.gdglille.devfest.android.theme.m3.schedules.test

import androidx.compose.ui.test.junit4.ComposeTestRule
import com.paligot.confily.core.test.patterns.navigation.NavRobotGraph
import com.paligot.confily.core.test.patterns.navigation.RobotNavigator
import com.paligot.confily.core.test.patterns.navigation.robot
import org.gdglille.devfest.android.theme.m3.schedules.test.robot.FiltersRobot
import org.gdglille.devfest.android.theme.m3.schedules.test.robot.ScheduleDetailsRobot
import org.gdglille.devfest.android.theme.m3.schedules.test.robot.ScheduleGridRobot
import org.gdglille.devfest.android.theme.m3.schedules.test.scopes.FiltersRobotScope
import org.gdglille.devfest.android.theme.m3.schedules.test.scopes.ScheduleDetailsRobotScope
import org.gdglille.devfest.android.theme.m3.schedules.test.scopes.ScheduleGridRobotScope

fun NavRobotGraph.scheduleRobotGraph(
    navigator: RobotNavigator,
    composeTestRule: ComposeTestRule
) {
    robot<ScheduleGridRobotScope>(ScheduleGridRobot(navigator, composeTestRule))
    robot<FiltersRobotScope>(FiltersRobot(navigator, composeTestRule))
    robot<ScheduleDetailsRobotScope>(ScheduleDetailsRobot(navigator, composeTestRule))
}
