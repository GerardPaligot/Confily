package com.paligot.confily.core.partners.entities

import com.paligot.confily.partners.ui.models.SalaryUi
import kotlin.native.ObjCName

@ObjCName("SalaryEntity")
class Salary(
    val min: Int,
    val max: Int,
    val recurrence: String
)

fun Salary.mapToSalaryUi(): SalaryUi = SalaryUi(
    min = min,
    max = max,
    recurrence = recurrence
)
