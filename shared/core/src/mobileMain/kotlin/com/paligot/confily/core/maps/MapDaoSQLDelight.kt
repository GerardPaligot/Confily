package com.paligot.confily.core.maps

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.paligot.confily.core.maps.entities.MapFilledItem
import com.paligot.confily.core.maps.entities.MapItemList
import com.paligot.confily.db.ConfilyDatabase
import com.paligot.confily.db.SelectFilledMaps
import com.paligot.confily.models.EventMap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlin.coroutines.CoroutineContext

class MapDaoSQLDelight(
    private val db: ConfilyDatabase,
    private val dispatcher: CoroutineContext
) : MapDao {
    override fun fetchMapItems(eventId: String): Flow<MapItemList> = combine(
        flow = db.mapQueries.selectMaps(eventId).asFlow().mapToList(dispatcher),
        flow2 = db.mapQueries.selectShapes(eventId).asFlow().mapToList(dispatcher),
        flow3 = db.mapQueries.selectPictograms(eventId).asFlow().mapToList(dispatcher),
        transform = { maps, shapes, pictograms ->
            MapItemList(
                items = maps.map { map -> map.mapToEntity(shapes, pictograms) }
            )
        }
    )

    override fun fetchMapFilled(eventId: String): Flow<List<MapFilledItem>> =
        db.mapQueries.selectFilledMaps(eventId)
            .asFlow().mapToList(dispatcher)
            .map { it.map(SelectFilledMaps::mapToEntity) }

    override fun insertMaps(eventId: String, maps: List<EventMap>) = db.transaction {
        maps.forEach { map ->
            db.mapQueries.insertMap(
                id = map.id,
                name = map.name,
                url = map.url,
                filled_url = map.filledUrl,
                order_ = map.order.toLong(),
                color = map.color,
                selected_color = map.colorSelected,
                picto_size = map.pictoSize.toLong(),
                event_id = eventId
            )
            map.shapes.forEach { shape ->
                db.mapQueries.insertShape(
                    order_ = shape.order.toLong(),
                    name = shape.name,
                    description = shape.description,
                    type = shape.type.name,
                    start_x = shape.start.x.toDouble(),
                    start_y = shape.start.y.toDouble(),
                    end_x = shape.end.x.toDouble(),
                    end_y = shape.end.y.toDouble(),
                    map_id = map.id,
                    event_id = eventId
                )
            }
            map.pictograms.forEach {
                db.mapQueries.insertPictogram(
                    order_ = it.order.toLong(),
                    name = it.name,
                    description = it.description,
                    type = it.type.name,
                    x = it.position.x.toDouble(),
                    y = it.position.y.toDouble(),
                    map_id = map.id,
                    event_id = eventId
                )
            }
        }
        val diff = db.mapQueries.diffMaps(event_id = eventId, id = maps.map { it.id })
            .executeAsList()
        db.mapQueries.deleteShapes(event_id = eventId, map_id = diff)
        db.mapQueries.deletePictograms(event_id = eventId, map_id = diff)
        db.mapQueries.deleteMaps(event_id = eventId, id = diff)
    }
}
