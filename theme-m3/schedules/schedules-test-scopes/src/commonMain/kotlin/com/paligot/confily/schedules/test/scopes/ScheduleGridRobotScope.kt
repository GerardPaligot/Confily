package com.paligot.confily.schedules.test.scopes

interface ScheduleGridRobotScope {
    fun addSessionToFavorites(
        sessionName: String,
        speakers: List<String>,
        room: String,
        duration: Int,
        categoryName: String
    )

    fun assertSessionIsShown(
        show: Boolean,
        sessionName: String,
        speakers: List<String>,
        room: String,
        duration: Int,
        categoryName: String
    )

    infix fun goToFilterScreen(block: FiltersRobotScope.() -> Unit): FiltersRobotScope

    infix fun goToSecheduleDetail(
        block: ScheduleDetailsRobotScope.() -> Unit
    ): ScheduleDetailsRobotScope
}
