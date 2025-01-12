package com.paligot.confily.core.partners

import com.paligot.confily.core.partners.entities.ActivityItem
import com.paligot.confily.core.partners.entities.JobItem
import com.paligot.confily.core.partners.entities.PartnerInfo
import com.paligot.confily.core.partners.entities.PartnerItem
import com.paligot.confily.core.partners.entities.PartnerType
import com.paligot.confily.core.partners.entities.Salary
import com.paligot.confily.core.schedules.entities.Address
import com.paligot.confily.core.socials.entities.Social
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.LocalDateTime

internal val partnerType = { order_: Long, name: String ->
    PartnerType(order = order_.toInt(), name = name)
}

internal val partnerItemMapper = { id: String, name: String, logoUrl: String ->
    PartnerItem(id = id, name = name, logoUrl = logoUrl)
}

internal val partnerMapper = { id: String, name: String, description: String, logoUrl: String,
    videoUrl: String?, formattedAddress: List<String>?, _: String?, latitude: Double?,
    longitude: Double? ->
    PartnerInfo(
        id = id,
        name = name,
        description = description,
        logoUrl = logoUrl,
        videoUrl = videoUrl,
        address = if (formattedAddress != null && latitude != null && longitude != null) {
            Address(
                formatted = formattedAddress.toImmutableList(),
                latitude = latitude,
                longitude = longitude
            )
        } else {
            null
        }
    )
}

internal val activityMapper = { name: String, startTime: String, _: String?, partnerName: String ->
    ActivityItem(
        name = name,
        partnerName = partnerName,
        startTime = LocalDateTime.parse(startTime)
    )
}

internal val jobsMapper = { url: String, _: String, _: String, title: String, companyName: String,
    location: String, salaryMin: Long?, salaryMax: Long?,
    salaryRecurrence: String?, requirements: Double, _: Long,
    propulsed: String ->
    JobItem(
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
}

internal val socialMapper = { url: String, type: String ->
    Social(url = url, type = type)
}
