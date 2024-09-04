package com.paligot.confily.schedules.test.robot

import androidx.compose.ui.test.junit4.ComposeTestRule
import com.paligot.confily.core.test.patterns.navigation.RobotNavigator
import com.paligot.confily.core.test.patterns.navigation.navigateTo
import com.paligot.confily.schedules.test.pom.FiltersPOM
import com.paligot.confily.schedules.test.scopes.FiltersRobotScope
import com.paligot.confily.schedules.test.scopes.ScheduleGridRobotScope

class FiltersRobot(
    private val robotNavigator: RobotNavigator,
    composeTestRule: ComposeTestRule
) : FiltersRobotScope {
    private val filtersPOM = FiltersPOM(composeTestRule)

    override fun selectOnlyFavorites() {
        filtersPOM.clickOnFavoritesOnly()
    }

    override fun assertFilterScreenIsOpened() {
        filtersPOM.assertFilterScreenIsOpened()
    }

    override infix fun backToScheduleGrid(
        block: ScheduleGridRobotScope.() -> Unit
    ): ScheduleGridRobotScope {
        filtersPOM.back()
        return robotNavigator.navigateTo<ScheduleGridRobotScope>().apply(block)
    }
}
