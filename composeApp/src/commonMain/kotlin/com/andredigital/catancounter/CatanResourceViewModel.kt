package com.andredigital.catancounter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class CatanResourceViewModel: ViewModel() {


    private val _cardStateFlow = MutableStateFlow(
        mutableMapOf<String, CatanResourceItem>()
    )
    val cardStateFlow: StateFlow<Map<String, CatanResourceItem>> = _cardStateFlow

    init {
        retrieveData()
        subscribeToChanges()
    }

    private fun subscribeToChanges() {
        viewModelScope.launch {
            cardStateFlow.collectLatest { states ->
                val items = states.entries.associate {
                    it.key to it.value.toEntity()
                }
                storeStateData(Json.encodeToString(items))
            }
        }
    }

    fun storeCountUpdates() {
        val items = cardStateFlow.value.entries.associate {
            it.key to it.value.toEntity()
        }
        storeStateData(Json.encodeToString(items))
    }

    private fun retrieveData() {
        retrieveStateData()?.let { json ->
            val states = Json.decodeFromString<Map<String, CatanResourceItemEntity>>(json)
            _cardStateFlow.value = states.entries.associate {
                it.key to CatanResourceItem.fromEntity(it.value)
            }.toMutableMap()
        }
    }

    fun addResource(resource: CatanResourceItem) {
        _cardStateFlow.update { map ->
            val resourceMap = map.toMutableMap()
            resourceMap[resource.getKey()] = resource
            resourceMap
        }
    }

    fun removeResource(key: String) {
        _cardStateFlow.update { map ->
            val resourceMap = map.toMutableMap()
            resourceMap.remove(key)
            resourceMap
        }
    }

    fun toCardStates(): List<CatanCardState> = cardStateFlow.value.values.toList()
        .groupBy { resource -> resource.rollNumber }
        .toList()
        .sortedBy { (rollNumber) -> rollNumber }
        .map { (rollNumber, resourceItems) ->
            CatanCardState(
                rollNumber = rollNumber,
                items = resourceItems
            )
        }.toList()
}