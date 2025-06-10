package com.paligot.confily.backend.map.domain

import com.paligot.confily.models.CreatedMap
import com.paligot.confily.models.EventMap
import com.paligot.confily.models.inputs.MapInput
import java.io.File

interface MapAdminRepository {
    suspend fun create(eventId: String, file: File): CreatedMap
    suspend fun updatePlan(eventId: String, mapId: String, file: File, filled: Boolean): EventMap
    fun update(eventId: String, mapId: String, input: MapInput): EventMap
    suspend fun delete(eventId: String, mapId: String)
}
