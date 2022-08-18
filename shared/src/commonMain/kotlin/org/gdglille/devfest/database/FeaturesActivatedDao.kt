package org.gdglille.devfest.database

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.gdglille.devfest.db.Conferences4HallDatabase
import org.gdglille.devfest.models.ScaffoldConfigUi

class FeaturesActivatedDao(private val db: Conferences4HallDatabase, private val eventId: String) {
    fun fetchFeatures(): Flow<ScaffoldConfigUi> = combine(
        db.featuresActivatedQueries.selectFeatures(eventId).asFlow().mapToOneOrNull(),
        db.userQueries.selectQrCode(eventId).asFlow().mapToOneOrNull(),
        transform = { features, qrCode ->
            ScaffoldConfigUi(
                hasNetworking = if (features?.has_networking != null) features.has_networking else false,
                hasSpeakerList = if (features?.has_speaker_list != null) features.has_speaker_list else false,
                hasPartnerList = if (features?.has_partner_list != null) features.has_partner_list else false,
                hasMenus = if (features?.has_menus != null) features.has_menus else false,
                hasQAndA = if (features?.has_qanda != null) features.has_qanda else false,
                hasBilletWebTicket = if (features?.has_billet_web_ticket != null) features.has_billet_web_ticket else false,
                hasProfile = qrCode != null
            )
        }
    )
}
