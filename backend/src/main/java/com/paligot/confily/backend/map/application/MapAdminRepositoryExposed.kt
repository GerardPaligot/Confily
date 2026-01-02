package com.paligot.confily.backend.map.application

import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import com.paligot.confily.backend.map.domain.MapAdminRepository
import com.paligot.confily.backend.map.infrastructure.exposed.MapEntity
import com.paligot.confily.backend.map.infrastructure.exposed.MapPictogramEntity
import com.paligot.confily.backend.map.infrastructure.exposed.MapPictogramsTable
import com.paligot.confily.backend.map.infrastructure.exposed.MapShapeEntity
import com.paligot.confily.backend.map.infrastructure.exposed.MapShapesTable
import com.paligot.confily.backend.map.infrastructure.exposed.toModel
import com.paligot.confily.backend.map.infrastructure.storage.MapStorage
import com.paligot.confily.models.CreatedMap
import com.paligot.confily.models.EventMap
import com.paligot.confily.models.inputs.MapInput
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.util.UUID

class MapAdminRepositoryExposed(
    private val database: Database,
    private val mapStorage: MapStorage
) : MapAdminRepository {
    override suspend fun create(eventId: String, file: File): CreatedMap {
        val eventUuid = UUID.fromString(eventId)
        val event = transaction(db = database) { EventEntity[eventUuid] }
        val upload = mapStorage.uploadMap(event.slug, file.name, file.readBytes())
        return transaction(db = database) {
            val mapEntity = MapEntity.new {
                this.event = event
                this.name = file.name
                this.filename = file.name
                this.url = upload.url
            }
            CreatedMap(id = mapEntity.id.value.toString(), url = upload.url)
        }
    }

    override suspend fun updatePlan(eventId: String, mapId: String, file: File, filled: Boolean): EventMap {
        val eventUuid = UUID.fromString(eventId)
        val mapUuid = UUID.fromString(mapId)
        val event = transaction(db = database) { EventEntity[eventUuid] }
        val upload = mapStorage.uploadMap(event.slug, file.name, file.readBytes())
        return transaction(db = database) {
            val mapEntity = MapEntity
                .findById(event.id.value, mapUuid)
                ?: throw NotFoundException("Map not found $mapId")
            if (filled) {
                mapEntity.filledUrl = upload.url
            } else {
                mapEntity.url = upload.url
            }
            val shapes = MapShapeEntity
                .findByMapId(mapEntity.id.value)
                .orderBy(MapShapesTable.displayOrder to SortOrder.ASC)
                .map { it.toModel() }
            val pictograms = MapPictogramEntity
                .findByMapId(mapEntity.id.value)
                .orderBy(MapPictogramsTable.displayOrder to SortOrder.ASC)
                .map { it.toModel() }
            mapEntity.toModel(shapes, pictograms)
        }
    }

    override fun update(eventId: String, mapId: String, input: MapInput): EventMap = transaction(db = database) {
        val eventUuid = UUID.fromString(eventId)
        val mapUuid = UUID.fromString(mapId)
        val mapEntity = MapEntity
            .findById(eventUuid, mapUuid)
            ?: throw NotFoundException("Map not found $mapId")

        mapEntity.name = input.name
        mapEntity.color = input.color
        mapEntity.colorSelected = input.colorSelected
        mapEntity.pictoSize = input.pictoSize
        mapEntity.displayOrder = input.order

        // Update shapes
        MapShapesTable.deleteWhere { MapShapesTable.mapId eq mapEntity.id }
        input.shapes.forEachIndexed { index, shapeInput ->
            MapShapeEntity.new {
                this.map = mapEntity
                this.displayOrder = index
                this.name = shapeInput.name
                this.description = shapeInput.description
                this.startX = shapeInput.start.x.toBigDecimal()
                this.startY = shapeInput.start.y.toBigDecimal()
                this.endX = shapeInput.end.x.toBigDecimal()
                this.endY = shapeInput.end.y.toBigDecimal()
                this.type = shapeInput.type
            }
        }

        // Update pictograms
        MapPictogramsTable.deleteWhere { MapPictogramsTable.mapId eq mapEntity.id }
        input.pictograms.forEachIndexed { index, pictogramInput ->
            MapPictogramEntity.new {
                this.map = mapEntity
                this.name = pictogramInput.name
                this.description = pictogramInput.description
                this.positionX = pictogramInput.position.x.toBigDecimal()
                this.positionY = pictogramInput.position.y.toBigDecimal()
                this.type = pictogramInput.type
                this.displayOrder = index
            }
        }

        val shapes = MapShapeEntity
            .findByMapId(mapEntity.id.value)
            .orderBy(MapShapesTable.displayOrder to SortOrder.ASC)
            .map { it.toModel() }
        val pictograms = MapPictogramEntity
            .findByMapId(mapEntity.id.value)
            .orderBy(MapPictogramsTable.displayOrder to SortOrder.ASC)
            .map { it.toModel() }
        mapEntity.toModel(shapes, pictograms)
    }

    override suspend fun delete(eventId: String, mapId: String) {
        val eventUuid = UUID.fromString(eventId)
        val mapUuid = UUID.fromString(mapId)
        val (event, filename) = transaction(db = database) {
            val event = EventEntity[eventUuid]
            val mapEntity = MapEntity
                .findById(eventUuid, mapUuid)
                ?: throw NotFoundException("Map not found $mapId")
            val filename = mapEntity.filename
            mapEntity.delete()
            Pair(event, filename)
        }
        mapStorage.delete(event.slug, mapId = filename)
    }
}
