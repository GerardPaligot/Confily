package org.gdglille.devfest.backend.third.parties.openplanner

import kotlinx.serialization.Serializable

@Serializable
data class OpenPlanner(
    val generatedAt: String,
    val event: EventOP,
    val speakers: List<SpeakerOP>,
    val sessions: List<SessionOP>
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
    val bio: String?,
    val photoUrl: String?,
    val email: String,
    val phone: String?,
    val company: String?,
    val geolocation: String?,
    val jobTitle: String?,
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
    val abstract: String?,
    val dateStart: String? = null,
    val dateEnd: String? = null,
    val durationMinutes: Int,
    val speakerIds: List<String>,
    val trackId: String?,
    val language: String?,
    val level: String?,
    val formatId: String?,
    val categoryId: String?
)
