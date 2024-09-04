package org.gdglille.devfest.android.theme.m3.schedules.test.robot

import androidx.compose.ui.test.junit4.ComposeTestRule
import com.paligot.confily.core.test.patterns.navigation.RobotNavigator
import com.paligot.confily.core.test.patterns.navigation.navigateTo
import com.paligot.confily.schedules.test.scopes.ScheduleDetailsRobotScope
import com.paligot.confily.schedules.test.scopes.ScheduleGridRobotScope
import org.gdglille.devfest.android.theme.m3.schedules.test.pom.ScheduleDetailsPOM

class ScheduleDetailsRobot(
    private val navigator: RobotNavigator,
    composeTestRule: ComposeTestRule
) : ScheduleDetailsRobotScope {
    private val scheduleDetailsPOM = ScheduleDetailsPOM(composeTestRule)

    override fun assertSessionDetails(
        title: String,
        abstract: String,
        speakers: List<String>,
        room: String,
        duration: Int,
        category: String
    ) {
        scheduleDetailsPOM.apply {
            assertMetadata(title, room, duration, category)
            assertAbstract(abstract)
            assertSpeakers(speakers)
        }
    }

    override fun backToScheduleGrid(block: ScheduleGridRobotScope.() -> Unit): ScheduleGridRobotScope {
        scheduleDetailsPOM.back()
        return navigator.navigateTo<ScheduleGridRobotScope>().apply(block)
    }
}
