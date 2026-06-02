package com.paligot.confily.core.kvalue

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.getBooleanFlow
import com.russhwolf.settings.coroutines.getStringOrNullFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalSettingsApi::class)
class ConferenceSettings(
    private val settings: ObservableSettings
) {
    fun fetchEventId(): Flow<String> =
        settings.getStringOrNullFlow("EVENT_ID").map { it ?: throw EventSavedException() }

    fun getEventId(): String =
        settings.getStringOrNull("EVENT_ID") ?: throw EventSavedException()

    fun insertEventId(eventId: String) = settings.putString("EVENT_ID", eventId)

    fun deleteEventId() = settings.remove("EVENT_ID")

    fun fetchOnlyFavoritesFlag() = settings.getBooleanFlow("ONLY_FAVORITES", false)

    fun getOnlyFavoritesFlag(): Boolean = settings.getBoolean("ONLY_FAVORITES", false)

    fun upsertOnlyFavoritesFlag(onlyFavorites: Boolean) =
        settings.putBoolean("ONLY_FAVORITES", onlyFavorites)

    fun getQuizDeviceId(): String? = settings.getStringOrNull("QUIZ_DEVICE_ID")

    fun putQuizDeviceId(deviceId: String) = settings.putString("QUIZ_DEVICE_ID", deviceId)

    fun fetchQuizUsername(): Flow<String?> = settings.getStringOrNullFlow("QUIZ_USERNAME")

    fun getQuizUsername(): String? = settings.getStringOrNull("QUIZ_USERNAME")

    fun putQuizUsername(username: String) = settings.putString("QUIZ_USERNAME", username)

    fun getQuizResults(): String? = settings.getStringOrNull("QUIZ_RESULTS")

    fun putQuizResults(resultsJson: String) = settings.putString("QUIZ_RESULTS", resultsJson)
}
