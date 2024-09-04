package com.paligot.confily.schedules.test.robot

import androidx.compose.ui.test.junit4.ComposeTestRule
import com.paligot.confily.core.test.patterns.navigation.RobotNavigator
import com.paligot.confily.core.test.patterns.navigation.navigateTo
import com.paligot.confily.schedules.test.pom.ScheduleGridPOM
import com.paligot.confily.schedules.test.scopes.FiltersRobotScope
import com.paligot.confily.schedules.test.scopes.ScheduleDetailsRobotScope
import com.paligot.confily.schedules.test.scopes.ScheduleGridRobotScope

fun schedules(
    navigator: RobotNavigator,
    block: ScheduleGridRobotScope.() -> Unit
) = navigator.navigateTo<ScheduleGridRobotScope>().apply(block)

class ScheduleGridRobot(
    private val navigator: RobotNavigator,
    composeTestRule: ComposeTestRule
) : ScheduleGridRobotScope {
    private val scheduleGridPOM = ScheduleGridPOM(composeTestRule)

    override fun addSessionToFavorites(
        sessionName: String,
        speakers: List<String>,
        room: String,
        duration: Int,
        categoryName: String
    ) {
        scheduleGridPOM.clickOnFavoriteIcon(sessionName, speakers, room, duration, categoryName)
    }

    override fun assertSessionIsShown(
        show: Boolean,
        sessionName: String,
        speakers: List<String>,
        room: String,
        duration: Int,
        categoryName: String
    ) {
        if (show) {
            scheduleGridPOM.hasSessionItem(sessionName, speakers, room, duration, categoryName)
        } else {
            scheduleGridPOM.hasNotSessionItem(sessionName, speakers, room, duration, categoryName)
        }
    }

    override infix fun goToFilterScreen(block: FiltersRobotScope.() -> Unit): FiltersRobotScope {
        scheduleGridPOM.clickOnFiltersIcon()
        return navigator.navigateTo<FiltersRobotScope>().apply(block)
    }

    override infix fun goToSecheduleDetail(
        block: ScheduleDetailsRobotScope.() -> Unit
    ): ScheduleDetailsRobotScope {
        scheduleGridPOM.clickOnFirstSessionItem()
        return navigator.navigateTo<ScheduleDetailsRobotScope>().apply(block)
    }
}
