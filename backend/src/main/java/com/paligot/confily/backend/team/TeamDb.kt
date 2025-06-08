package com.paligot.confily.backend.team

import com.paligot.confily.backend.internals.infrastructure.firestore.SocialEntity

data class TeamDb(
    val id: String = "",
    val order: Int = 0,
    val name: String = "",
    val bio: String = "",
    val photoUrl: String? = null,
    val role: String? = null,
    val socials: List<SocialEntity> = emptyList(),
    val teamName: String = ""
)
