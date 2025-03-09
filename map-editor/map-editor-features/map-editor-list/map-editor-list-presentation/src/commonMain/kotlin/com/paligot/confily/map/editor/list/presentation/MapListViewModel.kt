package com.paligot.confily.map.editor.list.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paligot.confily.core.api.ConferenceApi
import com.paligot.confily.map.editor.list.ui.models.MapCreationUi
import com.paligot.confily.map.editor.list.ui.models.MapItemUi
import com.paligot.confily.map.editor.list.ui.models.MapListUi
import com.paligot.confily.map.editor.list.ui.models.MapListUiState
import com.paligot.confily.models.EventMap
import com.paligot.confily.models.inputs.MapInput
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

data class MapListState(
    val loading: Boolean = false,
    val items: List<EventMap> = emptyList(),
    val name: String? = null,
    val order: String? = null,
    val planPath: Path? = null
)

class MapListViewModel(
    private val eventId: String,
    private val apiKey: String,
    private val api: ConferenceApi
) : ViewModel() {
    private val defaultTitle = "Untitled"

    private val state = MutableStateFlow(MapListState())
    val uiState: StateFlow<MapListUiState> = state
        .map { state ->
            MapListUiState(
                loading = state.loading,
                uiModel = MapListUi(
                    creation = state.planPath?.let {
                        MapCreationUi(
                            name = state.name ?: defaultTitle,
                            order = state.order ?: "",
                            planPath = it.toString()
                        )
                    } ?: run { null },
                    items = state.items
                        .map { MapItemUi(id = it.id, name = it.name, url = it.url) }
                        .toImmutableList()
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = MapListUiState()
        )

    init {
        loadMapList()
    }

    private fun loadMapList() {
        state.value = state.value.copy(loading = true)
        viewModelScope.launch {
            val items = api.fetchMapList(eventId)
            state.update { it.copy(loading = false, items = items) }
        }
    }

    fun dropMap(files: List<String>) {
        state.update {
            it.copy(
                name = defaultTitle,
                order = null,
                planPath = Path(files.first().removePrefix("file:"))
            )
        }
    }

    fun nameChange(value: String) {
        state.update { it.copy(name = value) }
    }

    fun orderChange(value: String) {
        val order = try {
            value.toInt().toString()
        } catch (e: NumberFormatException) {
            null
        }
        state.update { it.copy(order = order) }
    }

    fun cancelCreation() {
        state.update { it.copy(name = null, order = null, planPath = null) }
    }

    fun saveCreation() = viewModelScope.launch {
        state.update { it.copy(loading = true) }
        val localPath = state.value.planPath!!
        if (SystemFileSystem.exists(localPath).not()) {
            return@launch
        }
        SystemFileSystem.source(localPath).use { source ->
            val mapBytes = source.buffered().readByteArray()
            val created = api.createMap(eventId, apiKey, localPath.name, mapBytes)
            val input = MapInput(
                name = state.value.name ?: defaultTitle,
                order = state.value.order?.toInt() ?: 0,
                color = "#FFFFFF",
                colorSelected = "#FF0000"
            )
            api.updateMap(eventId, apiKey, created.id, input)
            val items = api.fetchMapList(eventId)
            state.update {
                it.copy(items = items, name = null, order = null, planPath = null, loading = false)
            }
        }
    }
}
