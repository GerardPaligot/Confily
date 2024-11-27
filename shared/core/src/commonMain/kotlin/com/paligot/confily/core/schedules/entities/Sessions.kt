package com.paligot.confily.core.schedules.entities

import com.paligot.confily.models.ui.AgendaUi
import com.paligot.confily.resources.Strings
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.byUnicodePattern
import kotlin.native.ObjCName

@ObjCName("SessionsEntity")
class Sessions(
    val filtersApplied: FiltersApplied,
    val sessions: Map<LocalDate, List<Item>>
)

fun Sessions.mapToMapUi(strings: Strings): ImmutableMap<String, AgendaUi> {
    return sessions
        .map { (date, items) ->
            date.format(
                LocalDate.Format {
                    byUnicodePattern("dd/MM/yyyy")
                }
            ) to AgendaUi(
                onlyFavorites = filtersApplied.onlyFavorites,
                sessions = items
                    .groupBy { it.startTime }
                    .map { entry ->
                        val slotTime = entry.key.format(
                            LocalDateTime.Format {
                                byUnicodePattern("HH:mm")
                            }
                        )
                        slotTime to entry.value
                            .sortedBy { it.order }
                            .map { it.mapToUi(strings) }
                            .toImmutableList()
                    }
                    .associate { it }
                    .toImmutableMap()
            )
        }
        .associate { it }
        .toImmutableMap()
}

fun Sessions.mapToListUi(strings: Strings): ImmutableList<AgendaUi> {
    return sessions
        .map { (_, items) ->
            AgendaUi(
                onlyFavorites = filtersApplied.onlyFavorites,
                sessions = items
                    .groupBy { it.startTime }
                    .map { entry ->
                        val slotTime = entry.key.format(
                            LocalDateTime.Format {
                                byUnicodePattern("HH:mm")
                            }
                        )
                        slotTime to entry.value
                            .sortedBy { it.order }
                            .map { it.mapToUi(strings) }
                            .toImmutableList()
                    }
                    .associate { it }
                    .toImmutableMap()
            )
        }
        .toImmutableList()
}
