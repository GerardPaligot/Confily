package com.paligot.confily.core.partners

import com.paligot.confily.models.ui.JobUi
import com.paligot.confily.models.ui.PartnerItemUi
import com.paligot.confily.models.ui.SalaryUi
import com.paligot.confily.models.ui.SocialTypeUi
import com.paligot.confily.models.ui.SocialUi
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

fun PartnerDb.toUi(): PartnerItemUi = PartnerItemUi(
    id = id,
    name = name,
    description = description,
    logoUrl = logoUrl,
    formattedAddress = formattedAddress?.toImmutableList(),
    address = address,
    latitude = latitude,
    longitude = longitude,
    socials = persistentListOf(),
    jobs = persistentListOf()
)

fun PartnerSocialDb.toUi(): SocialUi = SocialUi(
    url = url,
    type = SocialTypeUi.valueOf(type)
)

fun JobDb.toUi(): JobUi = JobUi(
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
