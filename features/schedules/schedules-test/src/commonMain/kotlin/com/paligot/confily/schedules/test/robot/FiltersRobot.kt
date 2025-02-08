package com.paligot.confily.schedules.test.robot

import androidx.compose.ui.test.junit4.ComposeTestRule
import com.paligot.confily.core.test.patterns.navigation.RobotNavigator
import com.paligot.confily.core.test.patterns.navigation.navigateTo
import com.paligot.confily.schedules.test.pom.FiltersPage
import com.paligot.confily.schedules.test.scopes.FiltersRobotScope
import com.paligot.confily.schedules.test.scopes.ScheduleGridRobotScope

class FiltersRobot(
    private val robotNavigator: RobotNavigator,
    composeTestRule: ComposeTestRule
) : FiltersRobotScope {
    private val filtersPage = FiltersPage(composeTestRule)

    override fun selectOnlyFavorites() {
        filtersPage.clickOnFavoritesOnly()
    }

    override fun assertFilterScreenIsOpened() {
        filtersPage.assertFilterScreenIsOpened()
    }

    override infix fun backToScheduleGrid(
        block: ScheduleGridRobotScope.() -> Unit
    ): ScheduleGridRobotScope {
        filtersPage.back()
        return robotNavigator.navigateTo<ScheduleGridRobotScope>().apply(block)
    }
}
