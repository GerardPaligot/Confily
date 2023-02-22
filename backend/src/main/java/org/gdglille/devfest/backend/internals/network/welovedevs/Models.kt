package org.gdglille.devfest.backend.internals.network.welovedevs

import kotlinx.serialization.Serializable

@Serializable
data class Salary(
    val min: Int,
    val max: Int,
    val recurrence: String = "year"
)

@Serializable
data class Details(
    val requiredExperience: Double,
    val salary: Salary? = null
)

@Serializable
data class Company(
    val id: String? = null,
    val companyName: String
)

@Serializable
data class Hit(
    val publishDate: Long,
    val objectID: String,
    val companyId: String,
    val title: String,
    val details: Details,
    val smallCompany: Company,
    val formattedPlaces: List<String> = emptyList()
)

@Serializable
data class PublicJobs(
    val hits: List<Hit>,
    val nbHits: Int,
    val page: Int,
    val nbPages: Int,
    val hitsPerPage: Int
)

@Serializable
data class JobQuery(
    val filters: String,
    val hitsPerPage: Int
)
