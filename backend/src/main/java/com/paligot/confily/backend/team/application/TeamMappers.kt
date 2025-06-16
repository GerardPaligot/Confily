package com.paligot.confily.backend.team.application

import com.paligot.confily.backend.internals.application.convertToEntity
import com.paligot.confily.backend.internals.application.convertToModel
import com.paligot.confily.backend.internals.infrastructure.firestore.TeamEntity
import com.paligot.confily.models.TeamMember
import com.paligot.confily.models.inputs.TeamMemberInput

fun TeamEntity.convertToModel() = TeamMember(
    id = id,
    order = order,
    displayName = name,
    bio = bio,
    role = role,
    photoUrl = photoUrl,
    socials = socials.map { it.convertToModel() }
)

fun TeamMemberInput.convertToEntity(
    lastOrder: Int?,
    photoUrl: String?,
    id: String? = null
) = TeamEntity(
    id = id ?: "",
    order = lastOrder?.plus(1) ?: this.order ?: 0,
    name = displayName,
    bio = bio,
    role = role,
    photoUrl = photoUrl,
    socials = socials.map { it.convertToEntity() },
    teamName = teamName
)
