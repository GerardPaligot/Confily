package com.paligot.confily.infos.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paligot.confily.core.maps.MapRepository
import com.paligot.confily.core.maps.entities.MapItemList
import com.paligot.confily.core.maps.entities.mapToMapUi
import com.paligot.confily.infos.ui.models.LabellingState
import com.paligot.confily.infos.ui.models.MapItemUiState
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class MapItemListUiState {
    data object Loading : MapItemListUiState()
    data class Success(val uiState: MapItemUiState) : MapItemListUiState()
    data class Failure(val throwable: Throwable) : MapItemListUiState()
}

class MapItemListViewModel(repository: MapRepository) : ViewModel() {
    private val state = MutableStateFlow(MapItemUiState())
    private val mapUi = repository.maps().map(MapItemList::mapToMapUi)
    val uiState: StateFlow<MapItemListUiState> = state.asStateFlow()
        .map { MapItemListUiState.Success(it) as MapItemListUiState }
        .catch { emit(MapItemListUiState.Failure(it)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = MapItemListUiState.Loading
        )

    init {
        viewModelScope.launch {
            mapUi.collect { newMapUi ->
                state.update { it.copy(uiModel = newMapUi) }
            }
        }
    }

    fun filterClick(mapId: String, label: String) = state.update { state ->
        if (label == "") return@update state
        val mapItem = state.uiModel.items.find { it.id == mapId } ?: return@update state
        mapItem.shapes.find { it.label == label }?.let {
            state.copy(
                labelling = state.labelling.toMutableMap().apply {
                    this[mapId] = LabellingState(label = label, description = it.description)
                }.toImmutableMap()
            )
        } ?: mapItem.pictograms.find { it.label == label }?.let {
            state.copy(
                labelling = state.labelling.toMutableMap().apply {
                    this[mapId] = LabellingState(label = label, it.description)
                }.toImmutableMap()
            )
        } ?: state
    }
}
