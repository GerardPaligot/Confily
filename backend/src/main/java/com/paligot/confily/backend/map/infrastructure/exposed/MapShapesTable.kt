package com.paligot.confily.backend.map.infrastructure.exposed

import com.paligot.confily.models.MappingType
import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object MapShapesTable : UUIDTable("map_shapes") {
    val mapId = reference("map_id", MapsTable, onDelete = ReferenceOption.CASCADE)
    val displayOrder = integer("display_order").default(0)
    val name = varchar("name", 255)
    val description = text("description").nullable()
    val startX = decimal("start_x", precision = 10, scale = 2)
    val startY = decimal("start_y", precision = 10, scale = 2)
    val endX = decimal("end_x", precision = 10, scale = 2)
    val endY = decimal("end_y", precision = 10, scale = 2)
    val type = enumeration<MappingType>("type")
    val createdAt = timestamp("created_at").clientDefault { Clock.System.now() }

    init {
        index(isUnique = false, mapId)
    }
}
