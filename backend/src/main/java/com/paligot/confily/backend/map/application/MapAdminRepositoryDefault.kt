package com.paligot.confily.backend.map.application

import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.internals.infrastructure.firestore.MapEntity
import com.paligot.confily.backend.internals.infrastructure.firestore.MapFirestore
import com.paligot.confily.backend.internals.infrastructure.storage.MapStorage
import com.paligot.confily.backend.map.domain.MapAdminRepository
import com.paligot.confily.models.CreatedMap
import com.paligot.confily.models.EventMap
import com.paligot.confily.models.MapPictogram
import com.paligot.confily.models.MapShape
import com.paligot.confily.models.inputs.MapInput
import java.io.File

class MapAdminRepositoryDefault(
    private val mapFirestore: MapFirestore,
    private val mapStorage: MapStorage
) : MapAdminRepository {
    override suspend fun create(eventId: String, file: File): CreatedMap {
        val url = mapStorage.uploadMap(eventId, file.name, file.readBytes()).url
        return CreatedMap(
            id = mapFirestore.createOrUpdate(eventId, MapEntity(filename = file.name, url = url)),
            url = url
        )
    }

    override suspend fun updatePlan(eventId: String, mapId: String, file: File, filled: Boolean): EventMap {
        val mapDb = mapFirestore.get(eventId, mapId) ?: throw NotFoundException("Map not found")
        val upload = mapStorage.uploadMap(eventId, file.name, file.readBytes())
        val newMapDb = if (filled) {
            mapDb.copy(filledUrl = upload.url)
        } else {
            mapDb.copy(url = upload.url)
        }
        mapFirestore.createOrUpdate(eventId, newMapDb)
        return newMapDb.convertToModel()
    }

    override fun update(eventId: String, mapId: String, input: MapInput): EventMap {
        val mapDb = mapFirestore.get(eventId, mapId) ?: throw NotFoundException("Map not found")
        mapFirestore.createOrUpdate(
            eventId = eventId,
            map = mapDb.copy(
                name = input.name,
                color = input.color,
                colorSelected = input.colorSelected,
                order = input.order,
                pictoSize = input.pictoSize,
                shapes = input.shapes.map(MapShape::convertToEntity),
                pictograms = input.pictograms.map(MapPictogram::convertToEntity)
            )
        )
        return mapFirestore.get(eventId, mapId)!!.convertToModel()
    }

    override suspend fun delete(eventId: String, mapId: String) {
        val filename = mapFirestore.delete(eventId, mapId)
        mapStorage.delete(eventId, filename)
    }
}
