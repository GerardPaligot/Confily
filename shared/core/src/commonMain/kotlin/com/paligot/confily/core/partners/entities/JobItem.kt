package com.paligot.confily.core.partners.entities

import com.paligot.confily.models.ui.JobUi
import kotlin.native.ObjCName

@ObjCName("JobItemEntity")
class JobItem(
    val title: String,
    val url: String,
    val partnerName: String,
    val location: String,
    val salary: Salary?,
    val requirements: Int,
    val propulsed: String
)

fun JobItem.mapToJobUi(): JobUi = JobUi(
    title = title,
    url = url,
    companyName = partnerName,
    location = location,
    salary = salary?.mapToSalaryUi(),
    requirements = requirements,
    propulsed = propulsed
)
