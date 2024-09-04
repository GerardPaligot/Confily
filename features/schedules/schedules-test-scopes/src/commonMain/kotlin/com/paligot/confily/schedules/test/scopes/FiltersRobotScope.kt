package com.paligot.confily.schedules.test.scopes

interface FiltersRobotScope {
    fun selectOnlyFavorites()
    fun assertFilterScreenIsOpened()
    infix fun backToScheduleGrid(block: ScheduleGridRobotScope.() -> Unit): ScheduleGridRobotScope
}
