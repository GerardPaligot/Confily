package com.paligot.confily.core.events.entities

import com.paligot.confily.core.socials.entities.Social
import com.paligot.confily.core.socials.entities.mapToSocialUi
import com.paligot.confily.infos.ui.models.TeamMemberUi
import kotlinx.collections.immutable.toImmutableList
import kotlin.native.ObjCName

@ObjCName("TeamMemberEntity")
class TeamMember(
    val info: TeamMemberInfo,
    val socials: List<Social>
)

fun TeamMember.mapToTeamMemberUi() = TeamMemberUi(
    id = info.id,
    displayName = info.displayName,
    bio = info.bio,
    role = info.role ?: "",
    photoUrl = info.photoUrl,
    socials = socials.map { it.mapToSocialUi() }.toImmutableList()
)
