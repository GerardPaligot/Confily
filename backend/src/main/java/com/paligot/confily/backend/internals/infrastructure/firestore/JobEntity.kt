package com.paligot.confily.backend.internals.infrastructure.firestore

data class SalaryEntity(
    val min: Int = 0,
    val max: Int = 0,
    val recurrence: String = ""
)

data class JobEntity(
    val id: String = "",
    val partnerId: String = "",
    val url: String = "",
    val title: String = "",
    val companyName: String = "",
    val location: String = "",
    val salary: SalaryEntity? = null,
    val requirements: Double = 0.0,
    val propulsed: String = "",
    val publishDate: Long = 0L
)
