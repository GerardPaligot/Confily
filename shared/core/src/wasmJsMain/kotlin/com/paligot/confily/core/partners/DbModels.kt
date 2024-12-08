package com.paligot.confily.core.partners

import kotlinx.serialization.Serializable

@Serializable
class PartnerDb(
    val id: String,
    val name: String,
    val description: String,
    val eventId: String,
    val type: String,
    val logoUrl: String,
    val formattedAddress: List<String>?,
    val address: String?,
    val latitude: Double?,
    val longitude: Double?
)

@Serializable
class PartnerTypeDb(
    val name: String,
    val order: Int,
    val eventId: String
)

@Serializable
class PartnerAndTypeDb(
    val id: String,
    val partnerId: String,
    val sponsorId: String,
    val eventId: String
)

@Serializable
class JobDb(
    val url: String,
    val partnerId: String,
    val eventId: String,
    val title: String,
    val companyName: String,
    val location: String,
    val salaryMin: Long?,
    val salaryMax: Long?,
    val salaryRecurrence: String?,
    val requirements: Double,
    val publishDate: Long,
    val propulsed: String
)
