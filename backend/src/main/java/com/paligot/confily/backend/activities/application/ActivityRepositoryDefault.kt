package com.paligot.confily.backend.activities.application

import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.activities.domain.ActivityRepository
import com.paligot.confily.backend.internals.infrastructure.firestore.ActivityFirestore
import com.paligot.confily.backend.internals.infrastructure.firestore.EventFirestore
import com.paligot.confily.backend.internals.infrastructure.firestore.PartnerFirestore
import com.paligot.confily.models.inputs.ActivityInput
import kotlinx.datetime.LocalDateTime

class ActivityRepositoryDefault(
    private val eventDao: EventFirestore,
    private val partnerFirestore: PartnerFirestore,
    private val activityFirestore: ActivityFirestore
) : ActivityRepository {
    override suspend fun create(eventId: String, activity: ActivityInput): String {
        val event = eventDao.get(eventId)
        val partnerExist = partnerFirestore.exists(eventId, activity.partnerId)
        if (!partnerExist) {
            throw NotFoundException("Partner ${activity.partnerId} Not Found")
        }
        val eventStartDate = LocalDateTime.Companion.parse(event.startDate.dropLast(1))
        val activityStartDate = LocalDateTime.Companion.parse(activity.startTime)
        if (activityStartDate < eventStartDate) {
            throw IllegalArgumentException("Activity start date must be after event start date")
        }
        activity.endTime?.let { endTime ->
            val eventEndDate = LocalDateTime.Companion.parse(event.endDate.dropLast(1))
            val activityEndDate = LocalDateTime.Companion.parse(endTime)
            if (activityEndDate > eventEndDate) {
                throw IllegalArgumentException("Activity end date must be before event end date")
            }
        }
        activityFirestore.createOrUpdate(eventId, activity.convertToEntity())
        return eventId
    }
}
