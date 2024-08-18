package org.gdglille.devfest.android.theme.m3.schedules.test.scopes

interface FiltersRobotScope {
    fun selectOnlyFavorites()
    fun assertFilterScreenIsOpened()
    infix fun backToScheduleGrid(block: ScheduleGridRobotScope.() -> Unit): ScheduleGridRobotScope
}
