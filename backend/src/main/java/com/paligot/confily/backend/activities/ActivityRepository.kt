package com.paligot.confily.backend.activities

import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.events.EventDao
import com.paligot.confily.backend.partners.PartnerDao
import com.paligot.confily.models.inputs.ActivityInput
import kotlinx.coroutines.coroutineScope
import kotlinx.datetime.LocalDateTime

class ActivityRepository(
    private val eventDao: EventDao,
    private val partnerDao: PartnerDao,
    private val activityDao: ActivityDao
) {
    suspend fun create(eventId: String, apiKey: String, activity: ActivityInput) = coroutineScope {
        val event = eventDao.getVerified(eventId, apiKey)
        val partnerExist = partnerDao.exists(eventId, activity.partnerId)
        if (!partnerExist) {
            throw NotFoundException("Partner ${activity.partnerId} Not Found")
        }
        val eventStartDate = LocalDateTime.parse(event.startDate.dropLast(1))
        val activityStartDate = LocalDateTime.parse(activity.startTime)
        if (activityStartDate < eventStartDate) {
            throw IllegalArgumentException("Activity start date must be after event start date")
        }
        activity.endTime?.let { endTime ->
            val eventEndDate = LocalDateTime.parse(event.endDate.dropLast(1))
            val activityEndDate = LocalDateTime.parse(endTime)
            if (activityEndDate > eventEndDate) {
                throw IllegalArgumentException("Activity end date must be before event end date")
            }
        }
        activityDao.createOrUpdate(eventId, activity.convertToDb())
        eventDao.updateUpdatedAt(event)
        return@coroutineScope eventId
    }
}
