package com.paligot.confily.backend.partners.infrastructure.exposed

import com.paligot.confily.models.Job
import com.paligot.confily.models.Salary

fun JobEntity.toModel(companyName: String): Job = Job(
    url = this.url,
    title = this.title,
    companyName = companyName,
    location = this.location ?: "",
    salary = if (this.salaryMin != null && this.salaryMax != null && this.salaryRecurrence != null) {
        Salary(
            min = this.salaryMin!!,
            max = this.salaryMax!!,
            recurrence = this.salaryRecurrence!!
        )
    } else {
        null
    },
    requirements = this.requirements?.toDouble() ?: 0.0,
    propulsed = this.propulsed ?: "",
    publishDate = this.publishDate?.toEpochDays()?.toLong() ?: 0L
)
