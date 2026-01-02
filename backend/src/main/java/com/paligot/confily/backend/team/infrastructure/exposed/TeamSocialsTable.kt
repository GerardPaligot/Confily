package com.paligot.confily.backend.team.infrastructure.exposed

import com.paligot.confily.backend.internals.infrastructure.exposed.SocialsTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll
import java.util.UUID

object TeamSocialsTable : Table("team_socials") {
    val teamMemberId = reference("team_member_id", TeamTable, onDelete = ReferenceOption.CASCADE)
    val socialId = reference("social_id", SocialsTable, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(teamMemberId, socialId)

    init {
        index(isUnique = false, teamMemberId)
        index(isUnique = false, socialId)
    }

    fun socialIds(teamMemberId: UUID): List<UUID> = this
        .selectAll()
        .where { TeamSocialsTable.teamMemberId eq teamMemberId }
        .map { it[socialId].value }
}
