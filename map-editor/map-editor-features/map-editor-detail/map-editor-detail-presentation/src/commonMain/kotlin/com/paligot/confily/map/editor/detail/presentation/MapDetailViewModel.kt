package com.paligot.confily.map.editor.detail.presentation

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.paligot.confily.core.api.ConferenceApi
import com.paligot.confily.map.editor.detail.routes.MapDetail
import com.paligot.confily.map.editor.detail.ui.models.ControllerUi
import com.paligot.confily.map.editor.detail.ui.models.MapDetailUiState
import com.paligot.confily.map.editor.detail.ui.models.MappingMode
import com.paligot.confily.map.editor.detail.ui.models.MappingUi
import com.paligot.confily.models.EventMap
import com.paligot.confily.models.MapPictogram
import com.paligot.confily.models.MapShape
import com.paligot.confily.models.inputs.MapInput
import com.paligot.confily.style.components.map.ui.models.MappingType
import com.paligot.confily.style.components.map.ui.models.OffsetUi
import com.paligot.confily.style.components.map.ui.models.PictogramType
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readByteArray

data class MapDetailState(
    val name: String? = null,
    val colorHex: String? = null,
    val colorHexSelected: String? = null,
    val pictoSize: Int = 4,
    val emptyPlanPath: Path? = null,
    val filledPlanPath: Path? = null,
    val interaction: InteractionState = InteractionState.Idle,
    val shapes: MutableList<MapShape> = mutableListOf(),
    val pictograms: MutableList<MapPictogram> = mutableListOf(),
    val eventMap: EventMap? = null,
    val enabled: Boolean = true
)

class MapDetailViewModel(
    stateHandle: SavedStateHandle,
    private val api: ConferenceApi
) : ViewModel() {
    private val route = stateHandle.toRoute<MapDetail>()
    private val state = MutableStateFlow(MapDetailState())
    val uiState: StateFlow<MapDetailUiState>
        get() = state
            .map { it.toUi() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = MapDetailUiState()
            )

    init {
        viewModelScope.launch {
            val items = api.fetchMapList(route.eventId)
            val map = items.find { it.id == route.mapId }
                ?: throw IllegalStateException("Map not found")
            state.update {
                it.copy(
                    name = map.name,
                    colorHex = map.color,
                    colorHexSelected = map.colorSelected,
                    pictoSize = map.pictoSize,
                    eventMap = map,
                    shapes = map.shapes.toMutableList(),
                    pictograms = map.pictograms.toMutableList()
                )
            }
        }
    }

    fun press(start: Offset) = state.update {
        state.value.interaction.press(start, state.value)
    }

    fun move(end: Offset, pressed: Boolean) = state.update {
        state.value.interaction.move(end, pressed, state.value)
    }

    fun end(end: Offset) = state.update {
        state.value.interaction.end(end, state.value)
    }

    fun pickEmptyFiles(files: List<String>) = state.update {
        it.copy(emptyPlanPath = Path(files.first().removePrefix("file:")))
    }

    fun pickFilledFiles(files: List<String>) = state.update {
        it.copy(filledPlanPath = Path(files.first().removePrefix("file:")))
    }

    fun nameChange(value: String) = state.update {
        it.copy(name = value)
    }

    fun colorChange(color: String) = state.update {
        it.copy(colorHex = color)
    }

    fun colorSelectedChange(color: String) = state.update {
        it.copy(colorHexSelected = color)
    }

    fun shapeOrderChange(value: String) {
        val interaction = state.value.interaction
        if (interaction is InteractionState.SelectionState) {
            state.update { interaction.orderChange(value, it) }
        }
    }

    fun shapeNameChange(value: String) {
        val interaction = state.value.interaction
        if (interaction is InteractionState.SelectionState) {
            state.update { interaction.nameChange(value, it) }
        }
    }

    fun shapeDescriptionChange(value: String) {
        val interaction = state.value.interaction
        if (interaction is InteractionState.SelectionState) {
            state.update { interaction.descriptionChange(value, it) }
        }
    }

    fun pictogramSizeChange(size: String) {
        val interaction = state.value.interaction
        if (interaction is InteractionState.PictogramState) {
            state.update { interaction.sizeChange(size, it) }
        }
    }

    fun modeClick(mode: MappingMode) {
        state.update {
            it.copy(
                interaction = when (mode) {
                    MappingMode.Draw -> InteractionState.ShapeDrawingState()
                    MappingMode.Suppression -> InteractionState.ShapeSuppressionState
                    MappingMode.Pictogram -> InteractionState.PictogramState()
                    MappingMode.Selection -> InteractionState.SelectionState()
                }
            )
        }
    }

    fun typeClick(type: MappingType) {
        val interaction = state.value.interaction
        if (interaction is InteractionState.ShapeDrawingState) {
            state.update { it.copy(interaction = interaction.copy(type = type)) }
        }
    }

    fun pictogramClick(pictogramType: PictogramType) {
        val interaction = state.value.interaction
        if (interaction is InteractionState.PictogramState) {
            state.update { it.copy(interaction = interaction.copy(type = pictogramType)) }
        }
    }

    fun save() = viewModelScope.launch {
        state.update { it.copy(enabled = false) }
        state.value.filledPlanPath?.let { updatePlan(it, true) }
        state.value.emptyPlanPath?.let { updatePlan(it, false) }
        val result = updateMap()
        state.update {
            it.copy(
                name = result.name,
                colorHex = result.color,
                colorHexSelected = result.colorSelected,
                pictoSize = result.pictoSize,
                shapes = result.shapes.toMutableList(),
                pictograms = result.pictograms.toMutableList(),
                emptyPlanPath = null,
                filledPlanPath = null,
                eventMap = result,
                enabled = true
            )
        }
    }

    private suspend fun updateMap(): EventMap = api.updateMap(
        eventId = route.eventId,
        apiKey = route.apiKey,
        mapId = route.mapId,
        input = MapInput(
            name = state.value.name ?: "Untitled",
            color = state.value.colorHex ?: "#FFFFFF",
            colorSelected = state.value.colorHexSelected ?: "#FF0000",
            order = 0,
            pictoSize = state.value.pictoSize,
            shapes = state.value.shapes,
            pictograms = state.value.pictograms
        )
    )

    private suspend fun updatePlan(localPath: Path, filled: Boolean) {
        if (SystemFileSystem.exists(localPath).not()) {
            return
        }
        SystemFileSystem.source(localPath).use { source ->
            val mapBytes = source.buffered().readByteArray()
            api.updateMapPlan(
                eventId = route.eventId,
                apiKey = route.apiKey,
                mapId = route.mapId,
                filled = filled,
                fileName = localPath.name,
                mapBytes = mapBytes
            )
        }
    }
}

private fun MapDetailState.toUi(): MapDetailUiState = MapDetailUiState(
    name = name ?: "",
    mapping = if (eventMap != null && interaction is InteractionState.ShapeDrawingState) {
        MappingUi(
            planUrl = eventMap.url,
            labelSelected = null,
            started = interaction.started,
            start = OffsetUi(interaction.start.x, interaction.start.y),
            end = OffsetUi(interaction.end.x, interaction.end.y),
            color = colorHex ?: "",
            selectedColor = colorHexSelected ?: "",
            pictoSize = pictoSize,
            shapes = shapes.map(MapShape::toUiShape).toImmutableList(),
            pictograms = pictograms.map(MapPictogram::toUiPictogram).toImmutableList()
        )
    } else if (eventMap != null) {
        MappingUi(
            planUrl = eventMap.url,
            labelSelected = if (interaction is InteractionState.SelectionState) {
                interaction.name
            } else {
                null
            },
            started = false,
            start = OffsetUi.Zero,
            end = OffsetUi.Zero,
            color = colorHex ?: "",
            selectedColor = colorHexSelected ?: "",
            pictoSize = pictoSize,
            shapes = shapes.map(MapShape::toUiShape).toImmutableList(),
            pictograms = pictograms.map(MapPictogram::toUiPictogram).toImmutableList()
        )
    } else {
        null
    },
    filledMapUrl = eventMap?.filledUrl,
    controller = ControllerUi(
        name = name ?: eventMap?.name ?: "",
        color = colorHex ?: "",
        colorSelected = colorHexSelected ?: "",
        shapeOrder = if (interaction is InteractionState.SelectionState) {
            interaction.order
        } else {
            ""
        },
        shapeName = if (interaction is InteractionState.SelectionState) {
            interaction.name
        } else {
            ""
        },
        shapeDescription = if (interaction is InteractionState.SelectionState) {
            interaction.description
        } else {
            null
        },
        modeSelected = interaction.mode,
        modes = MappingMode.entries.toImmutableList(),
        typeSelected = if (interaction is InteractionState.ShapeDrawingState) {
            interaction.type
        } else {
            null
        },
        types = MappingType.entries.toImmutableList(),
        pictogramSelected = if (interaction is InteractionState.PictogramState) {
            interaction.type
        } else {
            null
        },
        pictogramSize = if (interaction is InteractionState.PictogramState) {
            interaction.size
        } else {
            ""
        }
    ),
    enabled = eventMap != null && enabled
)
