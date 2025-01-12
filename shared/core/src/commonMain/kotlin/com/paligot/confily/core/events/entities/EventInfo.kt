package com.paligot.confily.core.events.entities

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.daysUntil
import kotlinx.datetime.format
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.plus
import kotlinx.datetime.until
import kotlin.native.ObjCName

@ObjCName("EventInfoEntity")
class EventInfo(
    val id: String,
    val name: String,
    val formattedAddress: List<String>,
    val latitude: Double,
    val longitude: Double,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val email: String?,
    val phone: String?,
    val faqUrl: String,
    val cocUrl: String
) {
    fun days(): List<LocalDate> {
        val startDate = startTime.date
        val endDate = endTime.date
        val days = mutableListOf<LocalDate>()
        for (i in 0 until startDate.daysUntil(endDate) + 1) {
            val currentDate = startDate.plus(i, DateTimeUnit.DAY)
            days.add(currentDate)
        }
        return days
    }
}

fun EventInfo.mapToDays(): List<String> =
    days().map { it.format(LocalDate.Format { byUnicodePattern("dd/MM") }) }
