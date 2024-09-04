package org.gdglille.devfest.android.theme.m3.schedules.test.robot

import androidx.compose.ui.test.junit4.ComposeTestRule
import com.paligot.confily.core.test.patterns.navigation.RobotNavigator
import com.paligot.confily.core.test.patterns.navigation.navigateTo
import org.gdglille.devfest.android.theme.m3.schedules.test.pom.ScheduleDetailsPOM
import org.gdglille.devfest.android.theme.m3.schedules.test.scopes.ScheduleDetailsRobotScope
import org.gdglille.devfest.android.theme.m3.schedules.test.scopes.ScheduleGridRobotScope

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
