package com.paligot.confily.core.partners

import com.paligot.confily.models.ui.ActivityUi
import com.paligot.confily.models.ui.PartnerGroupUi
import com.paligot.confily.models.ui.PartnerGroupsUi
import com.paligot.confily.models.ui.PartnerItemUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map

class PartnerDaoSettings(
    private val partnerQueries: PartnerQueries
) : PartnerDao {
    override fun fetchPartners(eventId: String): Flow<PartnerGroupsUi> =
        partnerQueries.selectPartnerTypes(eventId)
            .flatMapConcat { types ->
                combine(
                    flows = types.map { type ->
                        partnerQueries.selectPartners(eventId, name = type.name)
                            .map { partners -> partners.map { it.toUi() } }
                            .map { type.name to it }
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

    override fun fetchPartner(eventId: String, id: String): Flow<PartnerItemUi> = combine(
        flow = partnerQueries.selectPartner(eventId, id),
        flow2 = partnerQueries.selectJobs(eventId, id),
        flow3 = partnerQueries.selectSocials(eventId, id),
        transform = { partner, jobs, socials ->
            partner.toUi().copy(
                jobs = jobs.map { it.toUi() }.toImmutableList(),
                socials = socials.map { it.toUi() }.toImmutableList()
            )
        }
    )

    override fun fetchActivitiesByDay(eventId: String): Flow<ImmutableMap<String, ImmutableList<ActivityUi>>> {
        TODO("Not yet implemented")
    }

    override fun fetchActivitiesByPartner(
        eventId: String,
        partnerId: String
    ): Flow<ImmutableList<ActivityUi>> {
        TODO("Not yet implemented")
    }
}
