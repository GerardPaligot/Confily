package com.paligot.confily.core.partners

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import com.paligot.confily.db.ConfilyDatabase
import com.paligot.confily.models.ui.PartnerGroupUi
import com.paligot.confily.models.ui.PartnerGroupsUi
import com.paligot.confily.models.ui.PartnerItemUi
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlin.coroutines.CoroutineContext

class PartnerDaoSQLDelight(
    private val db: ConfilyDatabase,
    private val dispatcher: CoroutineContext
) : PartnerDao {
    override fun fetchPartners(eventId: String): Flow<PartnerGroupsUi> = db.transactionWithResult {
        return@transactionWithResult db.partnerQueries.selectPartnerTypes(eventId).asFlow()
            .mapToList(dispatcher).flatMapConcat { types ->
                return@flatMapConcat combine(
                    types.map { type ->
                        db.partnerQueries.selectPartners(
                            event_id = eventId,
                            name = type.name,
                            mapper = partnerMapper
                        ).asFlow().mapToList(dispatcher).map { type.name to it }
                    },
                    transform = { results ->
                        PartnerGroupsUi(
                            groups = types.map { type ->
                                PartnerGroupUi(
                                    type = type.name,
                                    partners = results.find { it.first == type.name }!!.second
                                        .toImmutableList()
                                )
                            }.toImmutableList()
                        )
                    }
                )
            }
    }

    override fun fetchPartner(eventId: String, id: String): Flow<PartnerItemUi> =
        db.partnerQueries.selectPartner(eventId, id, partnerMapper).asFlow().mapToOne(dispatcher)
            .combine(
                db.partnerQueries.selectJobs(eventId, id, jobsMapper).asFlow().mapToList(dispatcher)
            ) { partner, jobs ->
                partner.copy(jobs = jobs.toImmutableList())
            }
}
