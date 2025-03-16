package com.paligot.confily.backend.team

import com.paligot.confily.backend.internals.socials.SocialDb

data class TeamDb(
    val id: String = "",
    val order: Int = 0,
    val name: String = "",
    val bio: String = "",
    val photoUrl: String? = null,
    val role: String? = null,
    val socials: List<SocialDb> = emptyList(),
    val teamName: String = ""
)
