package com.paligot.confily.core.events

import com.paligot.confily.core.events.FeaturesActivatedQueries.Scopes.FEATURES
import com.paligot.confily.core.getSerializableScopedFlow
import com.paligot.confily.core.putSerializableScoped
import com.russhwolf.settings.ObservableSettings
import kotlinx.coroutines.flow.Flow

class FeaturesActivatedQueries(private val settings: ObservableSettings) {
    private object Scopes {
        const val FEATURES = "features"
    }

    fun insertFeatures(feature: FeaturesActivatedDb) {
        settings.putSerializableScoped(FEATURES, feature.eventId, feature)
    }

    fun selectFeatures(eventId: String): Flow<FeaturesActivatedDb?> =
        settings.getSerializableScopedFlow<FeaturesActivatedDb>(FEATURES, eventId)
}
