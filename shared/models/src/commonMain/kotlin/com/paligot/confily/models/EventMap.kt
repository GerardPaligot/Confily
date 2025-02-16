package com.paligot.confily.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class PictogramType {
    ArrowUp, ArrowDown, ArrowLeft, ArrowRight, Coffee, Restaurant, Parking
}

@Serializable
enum class MappingType { Event, Room }

@Serializable
data class Offset(val x: Float, val y: Float)

@Serializable
data class MapShape(
    val order: Int,
    val name: String,
    val description: String?,
    val start: Offset,
    val end: Offset,
    val type: MappingType
)

@Serializable
data class MapPictogram(
    val order: Int,
    val name: String,
    val description: String?,
    val position: Offset,
    val type: PictogramType
)

@Serializable
data class EventMap(
    val id: String,
    val name: String,
    val color: String,
    @SerialName("color_selected")
    val colorSelected: String,
    val order: Int,
    val url: String,
    @SerialName("filled_url")
    val filledUrl: String?,
    @SerialName("picto_size")
    val pictoSize: Int,
    val shapes: List<MapShape>,
    val pictograms: List<MapPictogram>
)
