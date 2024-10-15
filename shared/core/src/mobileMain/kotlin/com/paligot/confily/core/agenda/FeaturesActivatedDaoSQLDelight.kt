package com.paligot.confily.core.agenda

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrDefault
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.paligot.confily.db.ConfilyDatabase
import com.paligot.confily.models.ui.ScaffoldConfigUi
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlin.coroutines.CoroutineContext

class FeaturesActivatedDaoSQLDelight(
    private val db: ConfilyDatabase,
    private val dispatcher: CoroutineContext
) : FeaturesActivatedDao {
    override fun fetchScaffoldConfig(eventId: String?): Flow<ScaffoldConfigUi> {
        return if (eventId != null) {
            fetchFeatures(eventId)
        } else {
            flow { ScaffoldConfigUi() }
        }
    }

    private fun fetchFeatures(eventId: String): Flow<ScaffoldConfigUi> = combine(
        db.featuresActivatedQueries.selectFeatures(eventId).asFlow().mapToOneOrNull(dispatcher),
        db.userQueries.selectQrCode(eventId).asFlow().mapToOneOrNull(dispatcher),
        db.sessionQueries.selectDays(eventId).asFlow().mapToList(dispatcher),
        db.userQueries.countNetworking(eventId).asFlow()
            .mapToOneOrDefault(defaultValue = 0L, context = dispatcher),
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
