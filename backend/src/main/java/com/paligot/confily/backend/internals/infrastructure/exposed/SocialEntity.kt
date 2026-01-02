package com.paligot.confily.backend.internals.infrastructure.exposed

import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class SocialEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<SocialEntity>(SocialsTable)

    var eventId by SocialsTable.eventId
    var event by EventEntity.Companion referencedOn SocialsTable.eventId
    var platform by SocialsTable.platform
    var url by SocialsTable.url
    var createdAt by SocialsTable.createdAt
}
