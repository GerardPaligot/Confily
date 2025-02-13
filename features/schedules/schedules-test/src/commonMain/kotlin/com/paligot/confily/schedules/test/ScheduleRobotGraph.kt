package com.paligot.confily.schedules.test

import androidx.compose.ui.test.junit4.ComposeTestRule
import com.paligot.confily.core.test.patterns.navigation.NavRobotGraph
import com.paligot.confily.core.test.patterns.navigation.RobotNavigator
import com.paligot.confily.core.test.patterns.navigation.robot
import com.paligot.confily.schedules.test.robot.FiltersRobot
import com.paligot.confily.schedules.test.robot.ScheduleDetailsRobot
import com.paligot.confily.schedules.test.robot.ScheduleGridRobot
import com.paligot.confily.schedules.test.scopes.FiltersRobotScope
import com.paligot.confily.schedules.test.scopes.ScheduleDetailsRobotScope
import com.paligot.confily.schedules.test.scopes.ScheduleGridRobotScope

fun NavRobotGraph.scheduleRobotGraph(
    navigator: RobotNavigator,
    composeTestRule: ComposeTestRule
) {
    robot<ScheduleGridRobotScope>(ScheduleGridRobot(navigator, composeTestRule))
    robot<FiltersRobotScope>(FiltersRobot(navigator, composeTestRule))
    robot<ScheduleDetailsRobotScope>(ScheduleDetailsRobot(navigator, composeTestRule))
}
