package com.paligot.confily.backend.team.infrastructure.exposed

import com.paligot.confily.backend.internals.infrastructure.exposed.SocialsTable
import com.paligot.confily.models.SocialItem
import com.paligot.confily.models.TeamMember
import org.jetbrains.exposed.sql.selectAll

fun TeamEntity.toModel(): TeamMember {
    val socials = TeamSocialsTable
        .innerJoin(SocialsTable)
        .selectAll()
        .where { TeamSocialsTable.teamMemberId eq this@toModel.id }
        .map { row -> SocialItem(type = row[SocialsTable.platform], url = row[SocialsTable.url]) }
    return TeamMember(
        id = this.id.value.toString(),
        order = this.displayOrder,
        displayName = this.name,
        bio = this.bio ?: "",
        role = this.role,
        photoUrl = this.photoUrl,
        socials = socials
    )
}
