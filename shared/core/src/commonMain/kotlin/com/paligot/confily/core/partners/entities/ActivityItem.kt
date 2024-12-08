package com.paligot.confily.core.partners.entities

import com.paligot.confily.models.ui.ActivityUi
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.byUnicodePattern
import kotlin.native.ObjCName

@ObjCName("ActivityItemEntity")
class ActivityItem(
    val name: String,
    val partnerName: String,
    val startTime: LocalDateTime
)

fun ActivityItem.mapToActivityUi() = ActivityUi(
    activityName = name,
    partnerName = partnerName,
    startTime = startTime.format(
        LocalDateTime.Format {
            byUnicodePattern("HH:mm")
        }
    )
)
