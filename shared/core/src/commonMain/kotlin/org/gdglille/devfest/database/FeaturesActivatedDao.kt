package org.gdglille.devfest.database

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrDefault
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.getStringOrNullFlow
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import org.gdglille.devfest.db.Conferences4HallDatabase
import org.gdglille.devfest.models.ui.ScaffoldConfigUi

@OptIn(ExperimentalSettingsApi::class)
class FeaturesActivatedDao(
    private val db: Conferences4HallDatabase,
    private val settings: ObservableSettings
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun fetchFeatures(): Flow<ScaffoldConfigUi> = settings.getStringOrNullFlow("EVENT_ID")
        .flatMapConcat {
            if (it != null) {
                fetchFeatures(it)
            } else {
                flow { ScaffoldConfigUi() }
            }
        }

    private fun fetchFeatures(eventId: String): Flow<ScaffoldConfigUi> = combine(
        db.featuresActivatedQueries.selectFeatures(eventId).asFlow().mapToOneOrNull(Dispatchers.IO),
        db.userQueries.selectQrCode(eventId).asFlow().mapToOneOrNull(Dispatchers.IO),
        db.sessionQueries.selectDays(eventId).asFlow().mapToList(Dispatchers.IO),
        db.userQueries.countNetworking(eventId).asFlow()
            .mapToOneOrDefault(defaultValue = 0L, context = Dispatchers.IO),
        transform = { features, qrCode, days, countNetworking ->
            ScaffoldConfigUi(
                hasNetworking = if (features?.has_networking != null) features.has_networking else false,
                hasSpeakerList = if (features?.has_speaker_list != null) features.has_speaker_list else false,
                hasPartnerList = if (features?.has_partner_list != null) features.has_partner_list else false,
                hasMenus = if (features?.has_menus != null) features.has_menus else false,
                hasQAndA = if (features?.has_qanda != null) features.has_qanda else false,
                hasBilletWebTicket = if (features?.has_billet_web_ticket != null) features.has_billet_web_ticket else false,
                hasProfile = qrCode != null,
                agendaTabs = days.toImmutableList(),
                hasUsersInNetworking = countNetworking != 0L
            )
        }
    )
}
