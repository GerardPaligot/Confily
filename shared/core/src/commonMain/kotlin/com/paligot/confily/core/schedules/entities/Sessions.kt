package com.paligot.confily.core.schedules.entities

import com.paligot.confily.resources.Strings
import com.paligot.confily.schedules.panes.models.AgendaUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toLocalDateTime
import kotlin.native.ObjCName

@ObjCName("SessionsEntity")
class Sessions(
    val filtersApplied: FiltersApplied,
    val sessions: Map<LocalDate, List<Item>>
)

fun Sessions.mapToMapUi(strings: Strings): ImmutableMap<String, AgendaUi> = sessions
    .map { (date, items) ->
        date.format(LocalDate.Format { byUnicodePattern("dd/MM/yyyy") }) to AgendaUi(
            onlyFavorites = filtersApplied.onlyFavorites,
            sessions = items
                .groupBy { it.startTime }
                .map { entry ->
                    val slotTime = entry.key
                        .format(LocalDateTime.Format { byUnicodePattern("HH:mm") })
                    slotTime to entry.value
                        .sortedBy { it.order }
                        .map { it.mapToSessionItemUi(strings) }
                        .toImmutableList()
                }
                .associate { it }
                .toImmutableMap()
        )
    }
    .associate { it }
    .toImmutableMap()

fun Sessions.mapToListUi(strings: Strings): ImmutableList<AgendaUi> = sessions
    .map { (_, items) ->
        AgendaUi(
            onlyFavorites = filtersApplied.onlyFavorites,
            sessions = items
                .groupBy { it.startTime }
                .map { entry ->
                    val slotTime = entry.key
                        .format(LocalDateTime.Format { byUnicodePattern("HH:mm") })
                    slotTime to entry.value
                        .sortedBy { it.order }
                        .map { it.mapToSessionItemUi(strings) }
                        .toImmutableList()
                }
                .associate { it }
                .toImmutableMap()
        )
    }
    .toImmutableList()

fun Sessions.tabSelected(): Int? {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val daysSorted = sessions.keys.sorted()
    val index = daysSorted.indexOf(now.date)
    return if (index == -1) 0 else index
}

fun Sessions.isCurrentDay(): Boolean = tabSelected() != null

fun Sessions.closestTimeSlotKey(): String? {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val todayItems = sessions[now.date] ?: sessions.values.first()
    val times = todayItems
        .map { it.startTime }
        .distinct()
        .sorted()
    val currentMinutes = now.hour * 60 + now.minute
    var best: LocalDateTime? = null
    for (time in times) {
        val slotMinutes = time.hour * 60 + time.minute
        if (slotMinutes <= currentMinutes) {
            best = time
        } else {
            break
        }
    }
    val target = best ?: times.last()
    return target.format(LocalDateTime.Format { byUnicodePattern("HH:mm") })
}
