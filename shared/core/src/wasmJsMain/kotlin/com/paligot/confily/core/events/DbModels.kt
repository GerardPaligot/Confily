package com.paligot.confily.core.events

import com.paligot.confily.core.socials.SocialDb
import kotlinx.serialization.Serializable

@Serializable
class EventDb(
    val id: String,
    val name: String,
    val formattedAddress: List<String>,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val date: String,
    val coc: String,
    val openfeedbackProjectId: String?,
    val contactEmail: String?,
    val contactPhone: String?,
    val socials: List<SocialDb>,
    val faqUrl: String?,
    val cocUrl: String?,
    val updatedAt: Long
)

@Serializable
class EventItemDb(
    val id: String,
    val name: String,
    val date: String,
    val timestamp: Long,
    val past: Boolean
)

@Serializable
class FeaturesActivatedDb(
    val eventId: String,
    val hasNetworking: Boolean,
    val speakerList: Boolean,
    val hasPartnerList: Boolean,
    val hasMenus: Boolean,
    val hasQanda: Boolean,
    val hasBilletWebTicket: Boolean,
    val hasTeamMembers: Boolean
)

@Serializable
class CocDb(
    val url: String?,
    val coc: String?,
    val email: String?,
    val phone: String?
)

@Serializable
class MenuDb(
    val name: String,
    val dish: String,
    val accompaniment: String,
    val dessert: String,
    val eventId: String
)

@Serializable
class QAndADb(
    val order: Long,
    val eventId: String,
    val question: String,
    val response: String,
    val language: String
)

@Serializable
class QAndAActionDb(
    val id: String,
    val order: Long,
    val qandaId: Long,
    val eventId: String,
    val label: String,
    val url: String
)

@Serializable
class TeamMemberGroupDb(
    val name: String,
    val order: Int,
    val eventId: String
)

@Serializable
class TeamMemberDb(
    val eventId: String,
    val id: String,
    val name: String,
    val bio: String,
    val role: String?,
    val photoUrl: String?,
    val teamGroupId: String
)
