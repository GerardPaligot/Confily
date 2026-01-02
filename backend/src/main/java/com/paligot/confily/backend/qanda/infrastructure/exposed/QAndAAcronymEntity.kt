package com.paligot.confily.backend.qanda.infrastructure.exposed

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedIterable
import java.util.UUID

class QAndAAcronymEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<QAndAAcronymEntity>(QAndAAcronymsTable) {
        fun findByQAndAId(qandaId: UUID): SizedIterable<QAndAAcronymEntity> = this
            .find { QAndAAcronymsTable.qandaId eq qandaId }
    }

    var qandaId by QAndAAcronymsTable.qandaId
    var qanda by QAndAEntity referencedOn QAndAAcronymsTable.qandaId
    var key by QAndAAcronymsTable.key
    var value by QAndAAcronymsTable.value
    var createdAt by QAndAAcronymsTable.createdAt
}
