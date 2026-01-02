package com.paligot.confily.backend.activities.infrastructure.firestore

data class ActivityEntity(
    val id: String? = null,
    val name: String = "",
    val startTime: String = "",
    val endTime: String? = null,
    val partnerId: String = ""
)
