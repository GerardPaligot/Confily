package com.paligot.confily.backend.activities.application

import com.paligot.confily.backend.ForbiddenException
import com.paligot.confily.backend.activities.domain.ActivityRepository
import com.paligot.confily.backend.activities.infrastructure.exposed.ActivityEntity
import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import com.paligot.confily.backend.partners.infrastructure.exposed.PartnerEntity
import com.paligot.confily.models.inputs.ActivityInput
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class ActivityRepositoryExposed(private val database: Database) : ActivityRepository {
    override suspend fun create(eventId: String, activity: ActivityInput): String = transaction(db = database) {
        val event = EventEntity[UUID.fromString(eventId)]
        val activityStartTime = Instant.parse(activity.startTime)
        if (activityStartTime.toLocalDateTime(TimeZone.UTC).date < event.startDate) {
            throw ForbiddenException("Activity start date must be after event start date")
        }
        val activityEndTime = activity.endTime?.let { Instant.parse(it) }
        if (activityEndTime != null) {
            if (activityEndTime.toLocalDateTime(TimeZone.UTC).date > event.endDate) {
                throw ForbiddenException("Activity end date must be before event end date")
            }
        }
        val activityEntity = ActivityEntity.new {
            this.event = event
            this.partner = PartnerEntity[UUID.fromString(activity.partnerId)]
            this.name = activity.name
            this.startTime = activityStartTime
            this.endTime = activityEndTime
        }
        activityEntity.id.value.toString()
    }
}
