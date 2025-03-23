package com.paligot.confily.backend.map

import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.models.CreatedMap
import com.paligot.confily.models.EventMap
import com.paligot.confily.models.MapPictogram
import com.paligot.confily.models.MapShape
import com.paligot.confily.models.inputs.MapInput
import java.io.File

class MapRepository(private val mapDao: MapDao) {
    fun list(eventId: String): List<EventMap> = mapDao.getAll(eventId).map(MapDb::convertToModel)

    suspend fun create(eventId: String, file: File): CreatedMap {
        val url = mapDao.uploadMap(eventId, file.name, file.readBytes()).url
        return CreatedMap(
            id = mapDao.createOrUpdate(eventId, MapDb(filename = file.name, url = url)),
            url = url
        )
    }

    suspend fun updatePlan(eventId: String, mapId: String, file: File, filled: Boolean): EventMap {
        val mapDb = mapDao.get(eventId, mapId) ?: throw NotFoundException("Map not found")
        val upload = mapDao.uploadMap(eventId, file.name, file.readBytes())
        val newMapDb = if (filled) {
            mapDb.copy(filledUrl = upload.url)
        } else {
            mapDb.copy(url = upload.url)
        }
        mapDao.createOrUpdate(eventId, newMapDb)
        return newMapDb.convertToModel()
    }

    fun update(eventId: String, mapId: String, input: MapInput): EventMap {
        val mapDb = mapDao.get(eventId, mapId) ?: throw NotFoundException("Map not found")
        mapDao.createOrUpdate(
            eventId = eventId,
            map = mapDb.copy(
                name = input.name,
                color = input.color,
                colorSelected = input.colorSelected,
                order = input.order,
                pictoSize = input.pictoSize,
                shapes = input.shapes.map(MapShape::convertToDb),
                pictograms = input.pictograms.map(MapPictogram::convertToDb)
            )
        )
        return mapDao.get(eventId, mapId)!!.convertToModel()
    }

    suspend fun delete(eventId: String, mapId: String) {
        mapDao.delete(eventId, mapId)
    }
}
