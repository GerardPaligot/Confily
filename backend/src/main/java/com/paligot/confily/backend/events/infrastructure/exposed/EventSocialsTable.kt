package com.paligot.confily.backend.events.infrastructure.exposed

import com.paligot.confily.backend.internals.infrastructure.exposed.SocialsTable
import com.paligot.confily.models.SocialItem
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll
import java.util.UUID

object EventSocialsTable : Table("event_socials") {
    val eventId = reference("event_id", EventsTable, onDelete = ReferenceOption.CASCADE)
    val socialId = reference("social_id", SocialsTable, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(eventId, socialId)

    init {
        index(isUnique = false, eventId)
        index(isUnique = false, socialId)
    }

    fun socialIds(eventId: UUID): List<EntityID<UUID>> = this.selectAll()
        .where { EventSocialsTable.eventId eq eventId }
        .map { it[socialId] }

    fun findByEventId(eventId: UUID): List<SocialItem> = this.innerJoin(SocialsTable)
        .selectAll()
        .where { EventSocialsTable.eventId eq eventId }
        .mapNotNull { row -> SocialItem(type = row[SocialsTable.platform], url = row[SocialsTable.url]) }
}
