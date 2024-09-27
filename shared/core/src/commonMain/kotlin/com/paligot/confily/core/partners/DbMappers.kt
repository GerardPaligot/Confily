package com.paligot.confily.core.partners

import com.paligot.confily.models.ui.JobUi
import com.paligot.confily.models.ui.PartnerItemUi
import com.paligot.confily.models.ui.SalaryUi
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

internal val partnerMapper = { id: String, name: String, description: String, logoUrl: String,
    siteUrl: String?, twitterUrl: String?, twitterMessage: String?,
    linkedinUrl: String?, linkedinMessage: String?,
    formattedAddress: List<String>?, address: String?,
    latitude: Double?, longitude: Double? ->
    PartnerItemUi(
        id = id,
        name = name,
        description = description,
        logoUrl = logoUrl,
        siteUrl = siteUrl,
        twitterUrl = twitterUrl,
        twitterMessage = twitterMessage,
        linkedinUrl = linkedinUrl,
        linkedinMessage = linkedinMessage,
        formattedAddress = formattedAddress?.toImmutableList(),
        address = address,
        latitude = latitude,
        longitude = longitude,
        jobs = persistentListOf()
    )
}

internal val jobsMapper = { url: String, _: String, _: String, title: String, companyName: String,
    location: String, salaryMin: Long?, salaryMax: Long?,
    salaryRecurrence: String?, requirements: Double, _: Long,
    propulsed: String ->
    JobUi(
        url = url,
        title = title,
        companyName = companyName,
        location = location,
        salary = if (salaryMin != null && salaryMax != null && salaryRecurrence != null) {
            SalaryUi(
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
