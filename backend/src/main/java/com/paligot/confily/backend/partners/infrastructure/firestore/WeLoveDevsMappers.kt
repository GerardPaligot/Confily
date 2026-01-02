package com.paligot.confily.backend.partners.infrastructure.firestore

import com.paligot.confily.backend.partners.infrastructure.provider.Hit
import com.paligot.confily.models.Job
import com.paligot.confily.models.Salary

fun Hit.convertToEntity(id: String, partnerId: String) = JobEntity(
    id = id,
    partnerId = partnerId,
    url = "https://welovedevs.com/app/job/$objectID",
    title = this.title,
    companyName = this.smallCompany.companyName,
    location = this.formattedPlaces.first(),
    salary = this.details.salary?.let { salary ->
        SalaryEntity(
            min = salary.min,
            max = salary.max,
            recurrence = salary.recurrence
        )
    },
    requirements = this.details.requiredExperience,
    propulsed = "WeLoveDevs",
    publishDate = publishDate
)

fun JobEntity.convertToModel() = Job(
    url = url,
    title = title,
    companyName = companyName,
    location = location,
    salary = salary?.let { Salary(min = it.min, max = it.max, recurrence = it.recurrence) },
    requirements = requirements,
    propulsed = propulsed,
    publishDate = publishDate
)
