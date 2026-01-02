package com.paligot.confily.backend.team.infrastructure.firestore

import com.paligot.confily.backend.internals.infrastructure.firestore.SocialEntity

data class TeamEntity(
    val id: String = "",
    val order: Int = 0,
    val name: String = "",
    val bio: String = "",
    val photoUrl: String? = null,
    val role: String? = null,
    val socials: List<SocialEntity> = emptyList(),
    val teamName: String = ""
)
