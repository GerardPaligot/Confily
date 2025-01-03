package com.paligot.confily.partners.ui.models

data class ActivityUi(
    val activityName: String,
    val partnerName: String,
    val startTime: String
) {
    companion object {
        val fake = ActivityUi(
            activityName = "Devoxx Belgium 2022",
            partnerName = "GDG Lille",
            startTime = "10:00"
        )
    }
}
