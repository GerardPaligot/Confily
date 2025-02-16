package com.paligot.confily.backend.map

import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.events.EventDao
import com.paligot.confily.models.CreatedMap
import com.paligot.confily.models.EventMap
import com.paligot.confily.models.MapPictogram
import com.paligot.confily.models.MapShape
import com.paligot.confily.models.inputs.MapInput
import java.io.File

class MapRepository(
    private val eventDao: EventDao,
    private val mapDao: MapDao
) {
    fun list(eventId: String): List<EventMap> = mapDao.getAll(eventId).map(MapDb::convertToModel)

    suspend fun create(eventId: String, apiKey: String, file: File): CreatedMap {
        val eventDb = eventDao.getVerified(eventId, apiKey)
        try {
            val url = mapDao.uploadMap(eventId, file.name, file.readBytes()).url
            return CreatedMap(
                id = mapDao.createOrUpdate(eventId, MapDb(filename = file.name, url = url)),
                url = url
            )
        } finally {
            eventDao.updateUpdatedAt(eventDb)
        }
    }

    suspend fun updatePlan(
        eventId: String,
        apiKey: String,
        mapId: String,
        file: File,
        filled: Boolean
    ): EventMap {
        val eventDb = eventDao.getVerified(eventId, apiKey)
        try {
            val mapDb = mapDao.get(eventId, mapId) ?: throw NotFoundException("Map not found")
            val upload = mapDao.uploadMap(eventId, file.name, file.readBytes())
            val newMapDb = if (filled) {
                mapDb.copy(filledUrl = upload.url)
            } else {
                mapDb.copy(url = upload.url)
            }
            mapDao.createOrUpdate(eventId, newMapDb)
            return newMapDb.convertToModel()
        } finally {
            eventDao.updateUpdatedAt(eventDb)
        }
    }

    fun update(eventId: String, apiKey: String, mapId: String, input: MapInput): EventMap {
        val eventDb = eventDao.getVerified(eventId, apiKey)
        try {
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
        } finally {
            eventDao.updateUpdatedAt(eventDb)
        }
    }

    suspend fun delete(eventId: String, apiKey: String, mapId: String) {
        val eventDb = eventDao.getVerified(eventId, apiKey)
        try {
            mapDao.delete(eventId, mapId)
        } finally {
            eventDao.updateUpdatedAt(eventDb)
        }
    }
}
