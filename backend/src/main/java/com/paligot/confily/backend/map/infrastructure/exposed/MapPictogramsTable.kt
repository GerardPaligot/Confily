package com.paligot.confily.backend.map.infrastructure.exposed

import com.paligot.confily.models.PictogramType
import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object MapPictogramsTable : UUIDTable("map_pictograms") {
    val mapId = reference("map_id", MapsTable, onDelete = ReferenceOption.CASCADE)
    val displayOrder = integer("display_order").default(0)
    val name = varchar("name", 255)
    val description = text("description").nullable()
    val positionX = decimal("position_x", precision = 10, scale = 2)
    val positionY = decimal("position_y", precision = 10, scale = 2)
    val type = enumeration<PictogramType>("type")
    val createdAt = timestamp("created_at").clientDefault { Clock.System.now() }

    init {
        index(isUnique = false, mapId)
    }
}
