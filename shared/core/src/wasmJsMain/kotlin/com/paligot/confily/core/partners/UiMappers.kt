package com.paligot.confily.core.partners

import com.paligot.confily.core.events.entities.Social
import com.paligot.confily.core.partners.entities.JobItem
import com.paligot.confily.core.partners.entities.PartnerInfo
import com.paligot.confily.core.partners.entities.PartnerItem
import com.paligot.confily.core.partners.entities.PartnerType
import com.paligot.confily.core.partners.entities.Salary
import com.paligot.confily.core.schedules.entities.Address

fun PartnerTypeDb.mapToEntity(): PartnerType = PartnerType(order = order, name = name)

fun PartnerDb.mapToInfoEntity(): PartnerInfo = PartnerInfo(
    id = id,
    name = name,
    logoUrl = logoUrl,
    description = description,
    address = if (formattedAddress != null && longitude != null && latitude != null) {
        Address(
            formatted = formattedAddress,
            longitude = longitude,
            latitude = latitude
        )
    } else {
        null
    }
)

fun PartnerDb.mapToEntity(): PartnerItem = PartnerItem(
    id = id,
    name = name,
    logoUrl = logoUrl
)

fun PartnerSocialDb.mapToEntity(): Social = Social(url = url, type = type)

fun JobDb.mapToEntity(): JobItem = JobItem(
    url = url,
    title = title,
    partnerName = companyName,
    location = location,
    salary = if (salaryMin != null && salaryMax != null && salaryRecurrence != null) {
        Salary(
            min = salaryMin.toInt(),
            max = salaryMax.toInt(),
            recurrence = salaryRecurrence
        )
    } else {
        null
    },
    requirements = requirements.toInt(),
    propulsed = propulsed
)
