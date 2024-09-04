package com.paligot.confily.schedules.test.scopes

interface ScheduleDetailsRobotScope {
    fun assertSessionDetails(
        title: String,
        abstract: String,
        speakers: List<String>,
        room: String,
        duration: Int,
        category: String
    )

    infix fun backToScheduleGrid(block: ScheduleGridRobotScope.() -> Unit): ScheduleGridRobotScope
}
