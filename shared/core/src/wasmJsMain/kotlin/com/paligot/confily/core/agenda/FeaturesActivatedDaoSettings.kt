package com.paligot.confily.core.agenda

import com.paligot.confily.core.events.FeaturesActivatedQueries
import com.paligot.confily.core.schedules.SessionQueries
import com.paligot.confily.models.ui.ScaffoldConfigUi
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow

class FeaturesActivatedDaoSettings(
    private val featuresActivatedQueries: FeaturesActivatedQueries,
    private val sessionQueries: SessionQueries
) : FeaturesActivatedDao {
    override fun fetchScaffoldConfig(eventId: String?): Flow<ScaffoldConfigUi> {
        return if (eventId != null) {
            fetchFeatures(eventId)
        } else {
            flow { ScaffoldConfigUi() }
        }
    }

    private fun fetchFeatures(eventId: String): Flow<ScaffoldConfigUi> = combine(
        featuresActivatedQueries.selectFeatures(eventId),
        sessionQueries.selectDays(eventId),
        transform = { features, days ->
            ScaffoldConfigUi(
                hasNetworking = false,
                hasSpeakerList = true,
                hasPartnerList = features?.hasPartnerList ?: false,
                hasMenus = features?.hasMenus ?: false,
                hasQAndA = features?.hasQanda ?: false,
                hasBilletWebTicket = false,
                hasProfile = false,
                agendaTabs = days.toImmutableList(),
                hasUsersInNetworking = false
            )
        }
    )
}
