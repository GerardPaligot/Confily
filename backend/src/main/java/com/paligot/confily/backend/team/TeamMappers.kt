package com.paligot.confily.backend.team

import com.paligot.confily.backend.internals.socials.convertToDb
import com.paligot.confily.backend.internals.socials.convertToModel
import com.paligot.confily.models.TeamMember
import com.paligot.confily.models.inputs.TeamMemberInput

fun TeamDb.convertToModel() = TeamMember(
    id = id,
    order = order,
    displayName = name,
    bio = bio,
    role = role,
    photoUrl = photoUrl,
    socials = socials.map { it.convertToModel() }
)

fun TeamMemberInput.convertToDb(
    lastOrder: Int?,
    photoUrl: String?,
    id: String? = null
) = TeamDb(
    id = id ?: "",
    order = lastOrder?.plus(1) ?: this.order ?: 0,
    name = displayName,
    bio = bio,
    role = role,
    photoUrl = photoUrl,
    socials = socials.map { it.convertToDb() }
)
