package com.paligot.confily.backend.partners.infrastructure.exposed

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class JobEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<JobEntity>(JobsTable)

    var partnerId by JobsTable.partnerId
    var partner by PartnerEntity referencedOn JobsTable.partnerId
    var url by JobsTable.url
    var title by JobsTable.title
    var location by JobsTable.location
    var salaryMin by JobsTable.salaryMin
    var salaryMax by JobsTable.salaryMax
    var salaryRecurrence by JobsTable.salaryRecurrence
    var requirements by JobsTable.requirements
    var propulsed by JobsTable.propulsed
    var publishDate by JobsTable.publishDate
    var createdAt by JobsTable.createdAt
    var updatedAt by JobsTable.updatedAt
}
