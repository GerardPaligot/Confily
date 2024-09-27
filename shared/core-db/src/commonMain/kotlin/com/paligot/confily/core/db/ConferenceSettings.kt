package com.paligot.confily.core.db

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.getBooleanFlow
import com.russhwolf.settings.coroutines.getStringOrNullFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalSettingsApi::class, ExperimentalCoroutinesApi::class)
class ConferenceSettings(
    private val settings: ObservableSettings
) {
    fun fetchEventId(): Flow<String> =
        settings.getStringOrNullFlow("EVENT_ID").map { it ?: throw EventSavedException() }

    fun fetchEventIdOrNull(): Flow<String?> = settings.getStringOrNullFlow("EVENT_ID")

    fun getEventId(): String =
        settings.getStringOrNull("EVENT_ID") ?: throw EventSavedException()

    fun insertEventId(eventId: String) = settings.putString("EVENT_ID", eventId)

    fun deleteEventId() = settings.remove("EVENT_ID")

    fun fetchOnlyFavoritesFlag() = settings.getBooleanFlow("ONLY_FAVORITES", false)

    fun getOnlyFavoritesFlag(): Boolean = settings.getBoolean("ONLY_FAVORITES", false)

    fun upsertOnlyFavoritesFlag(onlyFavorites: Boolean) = settings.putBoolean("ONLY_FAVORITES", onlyFavorites)

    fun lastEtag(eventId: String): String? = settings.getStringOrNull("AGENDA_ETAG_$eventId")

    fun updateEtag(eventId: String, etag: String?) =
        etag?.let { settings.putString("AGENDA_ETAG_$eventId", it) }
}
