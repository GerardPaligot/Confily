package com.paligot.confily.core

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.getStringFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

inline fun <reified T> Settings.putSerializableScoped(scope: String, key: String, value: T) {
    putString("$scope:$key", Json.encodeToString<T>(value))
    addScope(scope, "$scope:$key")
}

fun Settings.removeScoped(scope: String, key: String) {
    remove("$scope:$key")
    removeScope(scope, "$scope:$key")
}

inline fun <reified T> Settings.getSerializableScopedOrNull(scope: String, key: String): T? =
    getStringOrNull("$scope:$key")?.let { Json.decodeFromString(it) }

inline fun <reified T> Settings.getAllSerializableScoped(scope: String): List<T> =
    getScopes(scope).mapNotNull { getSerializableScopedOrNull(scope, it) }

@OptIn(ExperimentalSettingsApi::class)
inline fun <reified T> ObservableSettings.getSerializableScopedFlow(
    scope: String,
    id: String
): Flow<T> = getStringFlow("$scope:$id", "").map { Json.decodeFromString<T>(it) }

inline fun <reified T> ObservableSettings.getAllSerializableScopedFlow(scope: String): List<Flow<T>> =
    getScopes(scope).map { getStringFlow(it, "").map { Json.decodeFromString(it) } }

inline fun <reified T> ObservableSettings.combineAllSerializableScopedFlow(
    scope: String,
    crossinline filter: (T) -> Boolean = { true }
): Flow<List<T>> = combine(
    flows = getAllSerializableScopedFlow(scope),
    transform = { sessions -> sessions.filter(filter) }
)

fun Settings.addScope(key: String, newId: String) {
    val scopes = getStringOrNull(key)
        ?.let { Json.decodeFromString<MutableList<String>>(it) }
        ?: mutableListOf()
    if (scopes.contains(newId)) return
    scopes.add(newId)
    putString(key, Json.encodeToString(scopes))
}

fun Settings.removeScope(key: String, id: String) {
    val scopes = getStringOrNull(key)
        ?.let { Json.decodeFromString<MutableList<String>>(it) }
        ?: return
    scopes.remove(id)
    putString(key, Json.encodeToString(scopes))
}

fun Settings.getScopes(key: String): List<String> =
    getStringOrNull(key)?.let { Json.decodeFromString(it) } ?: emptyList()
