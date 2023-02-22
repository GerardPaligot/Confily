package org.gdglille.devfest.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Salary(
    val min: Int,
    val max: Int,
    val recurrence: String?
)

@Serializable
data class Job(
    val url: String,
    val title: String,
    @SerialName("company_name")
    val companyName: String,
    val location: String,
    val salary: Salary?,
    val requirements: Double,
    val propulsed: String,
    @SerialName("publish_date")
    val publishDate: Long
)
