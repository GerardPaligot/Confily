package com.paligot.confily.core.agenda

import com.paligot.confily.models.ui.ScaffoldConfigUi
import kotlinx.coroutines.flow.Flow

interface FeaturesActivatedDao {
    fun fetchScaffoldConfig(eventId: String?): Flow<ScaffoldConfigUi>
}
