package com.paligot.confily.schedules.ui.models

data class CategoryUi(
    val id: String,
    val name: String,
    val color: String,
    val icon: String?
) {
    companion object {
        val fake = CategoryUi(id = "", name = "Cloud", color = "default", icon = "cloud")
    }
}
