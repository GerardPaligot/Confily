package com.paligot.confily.backend.third.parties.openplanner.infrastructure.provider

import kotlinx.serialization.Serializable

@Serializable
data class OpenPlanner(
    val generatedAt: String,
    val event: EventOP,
    val speakers: List<SpeakerOP>,
    val sessions: List<SessionOP>,
    val faq: List<FaqSectionOP>,
    val team: List<TeamOP>
)

@Serializable
data class EventOP(
    val id: String,
    val name: String,
    val scheduleVisible: Boolean,
    val dateStart: String,
    val dateEnd: String,
    val formats: List<FormatOP>,
    val categories: List<CategoryOP>,
    val tracks: List<TrackOP>
)

@Serializable
data class FormatOP(
    val id: String,
    val name: String,
    val description: String? = null,
    val durationMinutes: Int
)

@Serializable
data class CategoryOP(
    val id: String,
    val name: String,
    val color: String
)

@Serializable
data class TrackOP(
    val id: String,
    val name: String
)

@Serializable
data class SpeakerOP(
    val id: String,
    val name: String,
    val bio: String? = null,
    val photoUrl: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val company: String? = null,
    val pronouns: String? = null,
    val geolocation: String? = null,
    val jobTitle: String? = null,
    val socials: List<SocialOP>
)

@Serializable
data class SocialOP(
    val link: String,
    val icon: String,
    val name: String
)

@Serializable
data class SessionOP(
    val id: String,
    val title: String,
    val abstract: String? = null,
    val dateStart: String? = null,
    val dateEnd: String? = null,
    val durationMinutes: Int,
    val speakerIds: List<String>,
    val trackId: String?,
    val language: String? = null,
    val level: String? = null,
    val formatId: String? = null,
    val categoryId: String? = null
)

@Serializable
data class FaqSectionOP(
    val id: String,
    val name: String,
    val privateId: String? = null,
    val private: Boolean? = null,
    val order: Int,
    val share: Boolean? = null,
    val items: List<FaqItemOP>
)

@Serializable
data class FaqItemOP(
    val id: String,
    val order: Int,
    val question: String,
    val answer: String
)

@Serializable
data class TeamOP(
    val id: String,
    val name: String,
    val bio: String? = null,
    val photoUrl: String? = null,
    val role: String? = null,
    val socials: List<SocialOP>,
    val teamOrder: Int? = null,
    val team: String? = null
)
