package com.paligot.confily.map.editor.detail.presentation

import androidx.compose.ui.geometry.Offset
import com.paligot.confily.map.editor.detail.ui.models.MappingMode
import com.paligot.confily.models.MapPictogram
import com.paligot.confily.models.MapShape
import com.paligot.confily.style.components.map.ui.models.MappingType
import com.paligot.confily.style.components.map.ui.models.PictogramType

sealed class InteractionState(val mode: MappingMode?) {
    val pictoDivSize = 5
    open fun press(start: Offset, state: MapDetailState): MapDetailState = state
    open fun move(end: Offset, pressed: Boolean, state: MapDetailState): MapDetailState = state
    open fun end(end: Offset, state: MapDetailState): MapDetailState = state

    data class SelectionState(
        val order: String = "",
        val name: String = "",
        val description: String? = null,
        val shapeIndex: Int = -1,
        val pictoIndex: Int = -1,
        val prevOffset: Offset = Offset.Zero
    ) : InteractionState(MappingMode.Selection) {
        override fun press(start: Offset, state: MapDetailState): MapDetailState {
            val shapeIndex = state.shapes.indexOfFirst { start.intersect(it.start, it.end) }
            val pictoIndex = state.pictograms.indexOfFirst {
                val pictoStart = com.paligot.confily.models.Offset(
                    it.position.x - pictoDivSize,
                    it.position.y - pictoDivSize
                )
                val pictoEnd = com.paligot.confily.models.Offset(
                    it.position.x + pictoDivSize,
                    it.position.y + pictoDivSize
                )
                start.intersect(pictoStart, pictoEnd)
            }
            return state.copy(
                interaction = SelectionState(
                    order = if (shapeIndex != -1) state.shapes[shapeIndex].order.toString() else if (pictoIndex != -1) state.pictograms[pictoIndex].order.toString() else "",
                    name = if (shapeIndex != -1) state.shapes[shapeIndex].name else if (pictoIndex != -1) state.pictograms[pictoIndex].name else "",
                    description = if (shapeIndex != -1) state.shapes[shapeIndex].description else if (pictoIndex != -1) state.pictograms[pictoIndex].description else "",
                    shapeIndex = shapeIndex,
                    pictoIndex = pictoIndex,
                    prevOffset = start
                )
            )
        }

        override fun move(end: Offset, pressed: Boolean, state: MapDetailState): MapDetailState {
            if (pressed.not()) return state
            val deltaX = end.x - prevOffset.x
            val deltaY = end.y - prevOffset.y
            if (shapeIndex != -1) {
                val shape = state.shapes[shapeIndex]
                state.shapes[shapeIndex] = shape.copy(
                    start = shape.start.copy(
                        x = shape.start.x + deltaX,
                        y = shape.start.y + deltaY
                    ),
                    end = shape.end.copy(
                        x = shape.end.x + deltaX,
                        y = shape.end.y + deltaY
                    )
                )
            } else if (pictoIndex != -1) {
                val pictogram = state.pictograms[pictoIndex]
                state.pictograms[pictoIndex] = pictogram.copy(
                    position = pictogram.position.copy(x = end.x, y = end.y)
                )
            }
            return state.copy(
                interaction = copy(
                    shapeIndex = shapeIndex,
                    pictoIndex = pictoIndex,
                    prevOffset = end
                )
            )
        }

        fun orderChange(order: String, state: MapDetailState): MapDetailState {
            val newOrder = try {
                order.toInt()
            } catch (ex: NumberFormatException) {
                0
            }
            if (shapeIndex != -1) {
                state.shapes[shapeIndex] = state.shapes[shapeIndex].copy(order = newOrder)
            } else if (pictoIndex != -1) {
                state.pictograms[pictoIndex] = state.pictograms[pictoIndex].copy(order = newOrder)
            }
            return state.copy(interaction = copy(order = order))
        }

        fun nameChange(name: String, state: MapDetailState): MapDetailState {
            if (shapeIndex != -1) {
                state.shapes[shapeIndex] = state.shapes[shapeIndex].copy(name = name)
            } else if (pictoIndex != -1) {
                state.pictograms[pictoIndex] = state.pictograms[pictoIndex].copy(name = name)
            }
            return state.copy(interaction = copy(name = name))
        }

        fun descriptionChange(description: String, state: MapDetailState): MapDetailState {
            if (shapeIndex != -1) {
                state.shapes[shapeIndex] = state.shapes[shapeIndex].copy(description = description)
            } else if (pictoIndex != -1) {
                state.pictograms[pictoIndex] = state.pictograms[pictoIndex].copy(description = description)
            }
            return state.copy(interaction = copy(description = description))
        }
    }

    data class ShapeDrawingState(
        val started: Boolean = false,
        val start: Offset = Offset.Zero,
        val end: Offset = Offset.Zero,
        val type: MappingType = MappingType.Event
    ) : InteractionState(MappingMode.Draw) {
        override fun press(start: Offset, state: MapDetailState): MapDetailState =
            state.copy(interaction = copy(started = true, start = start))

        override fun move(end: Offset, pressed: Boolean, state: MapDetailState): MapDetailState {
            if (started) {
                return state.copy(interaction = copy(end = end))
            }
            return state
        }

        override fun end(end: Offset, state: MapDetailState): MapDetailState {
            val shapes = state.shapes + MapShape(
                order = 0,
                name = "",
                description = null,
                start = start.toSerialOffset(),
                end = end.toSerialOffset(),
                type = type.toNetMappingType()
            )
            return state.copy(
                interaction = ShapeDrawingState(),
                shapes = shapes.toMutableList()
            )
        }
    }

    data object ShapeSuppressionState : InteractionState(MappingMode.Suppression) {
        override fun end(end: Offset, state: MapDetailState): MapDetailState = state.copy(
            interaction = ShapeSuppressionState,
            shapes = state.shapes
                .filter { end.intersect(it.start, it.end).not() }
                .toMutableList(),
            pictograms = state.pictograms
                .filter {
                    val pictoStart = com.paligot.confily.models.Offset(
                        it.position.x - pictoDivSize,
                        it.position.y - pictoDivSize
                    )
                    val pictoEnd = com.paligot.confily.models.Offset(
                        it.position.x + pictoDivSize,
                        it.position.y + pictoDivSize
                    )
                    end.intersect(pictoStart, pictoEnd).not()
                }
                .toMutableList()
        )
    }

    data class PictogramState(
        val size: String = "4",
        val type: PictogramType = PictogramType.ArrowUp
    ) : InteractionState(MappingMode.Pictogram) {
        override fun end(end: Offset, state: MapDetailState): MapDetailState {
            val pictograms = state.pictograms + MapPictogram(
                order = 0,
                name = "",
                description = null,
                position = end.toSerialOffset(),
                type = type.toNetPictogramType()
            )
            return state.copy(pictograms = pictograms.toMutableList())
        }

        fun sizeChange(size: String, state: MapDetailState): MapDetailState {
            return try {
                state.copy(
                    pictoSize = size.toInt(),
                    interaction = copy(size = size)
                )
            } catch (e: NumberFormatException) {
                state.copy(interaction = copy(size = ""))
            }
        }
    }

    data object Idle : InteractionState(mode = null)
}

private fun Offset.intersect(
    start: com.paligot.confily.models.Offset,
    end: com.paligot.confily.models.Offset
): Boolean = start.x < x && x < end.x && start.y < y && y < end.y
