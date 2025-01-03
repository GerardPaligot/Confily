package com.paligot.confily.core.events.entities

import com.paligot.confily.infos.ui.models.TeamMemberItemUi
import kotlin.native.ObjCName

@ObjCName("TeamMemberItemEntity")
class TeamMemberItem(
    val id: String,
    val displayName: String,
    val photoUrl: String?,
    val role: String?
)

fun TeamMemberItem.mapToTeamMemberItemUi() = TeamMemberItemUi(
    id = id,
    displayName = displayName,
    role = role ?: "",
    url = photoUrl
)
