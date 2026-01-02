package com.paligot.confily.backend.qanda.infrastructure.exposed

import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object QAndAAcronymsTable : UUIDTable("qanda_acronyms") {
    val qandaId = reference("qanda_id", QAndATable, onDelete = ReferenceOption.CASCADE)
    val key = varchar("key", 50)
    val value = text("value")
    val createdAt = timestamp("created_at").clientDefault { Clock.System.now() }

    init {
        index(isUnique = false, qandaId)
    }
}
