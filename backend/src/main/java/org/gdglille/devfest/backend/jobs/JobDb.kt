package org.gdglille.devfest.backend.jobs

data class SalaryDb(
    val min: Int = 0,
    val max: Int = 0,
    val recurrence: String = ""
)

data class JobDb(
    val id: String = "",
    val partnerId: String = "",
    val url: String = "",
    val title: String = "",
    val companyName: String = "",
    val location: String = "",
    val salary: SalaryDb? = null,
    val requirements: Double = 0.0,
    val propulsed: String = "",
    val publishDate: Long = 0L
)
