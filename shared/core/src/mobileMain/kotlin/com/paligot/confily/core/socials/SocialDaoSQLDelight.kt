package com.paligot.confily.core.socials

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.paligot.confily.core.socials.entities.Social
import com.paligot.confily.db.ConfilyDatabase
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

class SocialDaoSQLDelight(
    private val db: ConfilyDatabase,
    private val dispatcher: CoroutineContext
) : SocialDao {
    override fun fetchSocials(eventId: String, extId: String): Flow<List<Social>> =
        db.socialQueries
            .selectSocials(eventId, extId, socialMapper)
            .asFlow()
            .mapToList(dispatcher)
}
