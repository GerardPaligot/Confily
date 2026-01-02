package com.paligot.confily.backend.speakers.infrastructure.exposed

import com.paligot.confily.backend.internals.infrastructure.exposed.SocialsTable
import com.paligot.confily.models.SocialItem
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll
import java.util.UUID

object SpeakerSocialsTable : Table("speaker_socials") {
    val speakerId = reference("speaker_id", SpeakersTable, onDelete = ReferenceOption.CASCADE)
    val socialId = reference("social_id", SocialsTable, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(speakerId, socialId)

    init {
        index(isUnique = false, speakerId)
        index(isUnique = false, socialId)
    }

    fun socialIds(speakerId: UUID): List<EntityID<UUID>> = this
        .selectAll()
        .where { SpeakerSocialsTable.speakerId eq speakerId }
        .map { it[socialId] }

    fun socials(speakerId: UUID): List<SocialItem> = this
        .innerJoin(SocialsTable)
        .selectAll()
        .where { SpeakerSocialsTable.speakerId eq speakerId }
        .mapNotNull { row -> SocialItem(type = row[SocialsTable.platform], url = row[SocialsTable.url]) }
}
