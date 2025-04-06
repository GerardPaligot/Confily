package com.paligot.confily.core.partners.entities

import com.paligot.confily.partners.ui.models.PartnerGroupUi
import com.paligot.confily.partners.ui.models.PartnerGroupsUi
import com.paligot.confily.partners.ui.models.PartnersActivitiesUi
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.byUnicodePattern
import kotlin.native.ObjCName

@ObjCName("PartnersEntity")
class Partners(
    val groups: Map<PartnerType, List<PartnerItem>>,
    val activities: List<ActivityItem>
)

fun Partners.mapToPartnersActivitiesUi(): PartnersActivitiesUi = PartnersActivitiesUi(
    partners = PartnerGroupsUi(
        groups = groups
            .map { (type, partners) ->
                PartnerGroupUi(
                    type = type.name
                        .split(" ")
                        .joinToString(" ") { it.replaceFirstChar { it.uppercase() } },
                    partners = partners.map { it.mapToPartnerItemUi() }.toImmutableList()
                )
            }
            .toImmutableList()
    ),
    activities = activities
        .groupBy {
            it.startTime.format(
                LocalDateTime.Format {
                    byUnicodePattern("HH:mm")
                }
            )
        }
        .map { entry ->
            entry.key to entry.value.map { it.mapToActivityUi() }.toImmutableList()
        }
        .associate { it }
        .toImmutableMap()
)
