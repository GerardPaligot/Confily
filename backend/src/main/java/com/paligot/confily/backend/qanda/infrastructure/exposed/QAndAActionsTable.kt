package com.paligot.confily.backend.qanda.infrastructure.exposed

import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object QAndAActionsTable : UUIDTable("qanda_actions") {
    val qandaId = reference("qanda_id", QAndATable, onDelete = ReferenceOption.CASCADE)
    val displayOrder = integer("display_order").default(0)
    val label = varchar("label", 255)
    val url = text("url")
    val createdAt = timestamp("created_at").clientDefault { Clock.System.now() }

    init {
        index(isUnique = false, qandaId)
    }
}
