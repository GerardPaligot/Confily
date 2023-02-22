package org.gdglille.devfest.models

data class SalaryUi(
    val min: Int,
    val max: Int,
    val recurrence: String
) {
    companion object {
        val fake = SalaryUi(
            min = 55,
            max = 75,
            recurrence = "year"
        )
    }
}

data class JobUi(
    val url: String,
    val title: String,
    val companyName: String,
    val location: String,
    val salary: SalaryUi?,
    val requirements: Int,
    val propulsed: String
) {
    companion object {
        val fake = JobUi(
            url = "https://devfest.gdglille.org/",
            title = "Mobile Staff Engineer",
            companyName = "Google",
            location = "Paris, France",
            salary = SalaryUi.fake,
            requirements = 8,
            propulsed = "WeLoveDevs"
        )
    }
}
