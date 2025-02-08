package com.paligot.confily.schedules.test.robot

import androidx.compose.ui.test.junit4.ComposeTestRule
import com.paligot.confily.core.test.patterns.navigation.RobotNavigator
import com.paligot.confily.core.test.patterns.navigation.navigateTo
import com.paligot.confily.schedules.test.pom.ScheduleDetailsPage
import com.paligot.confily.schedules.test.scopes.ScheduleDetailsRobotScope
import com.paligot.confily.schedules.test.scopes.ScheduleGridRobotScope

class ScheduleDetailsRobot(
    private val navigator: RobotNavigator,
    composeTestRule: ComposeTestRule
) : ScheduleDetailsRobotScope {
    private val scheduleDetailsPage = ScheduleDetailsPage(composeTestRule)

    override fun assertSessionDetails(
        title: String,
        abstract: String,
        speakers: List<String>,
        room: String,
        duration: Int,
        category: String
    ) {
        scheduleDetailsPage.apply {
            assertMetadata(title, room, duration, category)
            assertAbstract(abstract)
            assertSpeakers(speakers)
        }
    }

    override fun backToScheduleGrid(block: ScheduleGridRobotScope.() -> Unit): ScheduleGridRobotScope {
        scheduleDetailsPage.back()
        return navigator.navigateTo<ScheduleGridRobotScope>().apply(block)
    }
}
